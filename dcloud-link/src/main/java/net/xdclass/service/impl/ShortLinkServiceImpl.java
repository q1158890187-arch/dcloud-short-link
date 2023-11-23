package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.component.ShortLinkComponent;
import net.xdclass.config.RabbitMQConfig;
import net.xdclass.controller.request.ShortLinkAddRequest;
import net.xdclass.enums.DomainTypeEnum;
import net.xdclass.enums.EventMessageType;
import net.xdclass.enums.ShortLinkStateEnum;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manage.DomainManager;
import net.xdclass.manage.GroupCodeMappingManager;
import net.xdclass.manage.LinkGroupManager;
import net.xdclass.manage.ShortLinkManager;
import net.xdclass.model.*;
import net.xdclass.service.ShortLinkService;
import net.xdclass.util.CommonUtil;
import net.xdclass.util.JsonData;
import net.xdclass.util.JsonUtil;
import net.xdclass.vo.ShortLinkVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/19 15:09
 */
@Service
@Slf4j
public class ShortLinkServiceImpl implements ShortLinkService {

    @Resource
    private ShortLinkManager shortLinkManager;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RabbitMQConfig rabbitMQConfig;
    @Resource
    private DomainManager domainManager;
    @Resource
    private LinkGroupManager linkGroupManager;

    @Resource
    private ShortLinkComponent shortLinkComponent;

    @Autowired
    private GroupCodeMappingManager groupCodeMappingManager;


    @Override
    public ShortLinkVO parseShortLinkCode(String shortLinkCode) {

        ShortLinkDO shortLinkDO = shortLinkManager.findByShortLinCode(shortLinkCode);
        if (shortLinkDO == null) {
            return null;
        }
        ShortLinkVO shortLinkVO = new ShortLinkVO();
        BeanUtils.copyProperties(shortLinkDO, shortLinkVO);
        return shortLinkVO;
    }

    @Override
    public JsonData createShortLink(ShortLinkAddRequest request) {

        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        EventMessage eventMessage = EventMessage.builder().accountNo(accountNo)
                .content(JsonUtil.obj2Json(request))
                .eventMessageType(EventMessageType.SHORT_LINK_ADD.name())
                .build();

        rabbitTemplate.convertAndSend(rabbitMQConfig.getShortLinkEventExchange(), rabbitMQConfig.getShortLinkAddRoutingKey(), eventMessage);
        return JsonData.buildSuccess();
    }

    /**
     * 处理短链新增逻辑
     * <p>
     * //判断短链域名是否合法
     * //判断组名是否合法
     * //生成长链摘要
     * //生成短链码
     * //加锁
     * //查询短链码是否存在
     * //构建短链对象
     * //保存数据库
     *
     * @param eventMessage
     * @return
     */
    @Override
    public boolean handlerAddShortLink(EventMessage eventMessage) {

        Long accountNo = eventMessage.getAccountNo();
        String messageType = eventMessage.getEventMessageType();

        ShortLinkAddRequest addRequest = JsonUtil.json2Obj(eventMessage.getContent(), ShortLinkAddRequest.class);
        // 短链域名校验
        DomainDO domainDO = checkDomain(addRequest.getDomainType(), addRequest.getDomainId(), accountNo);
        // 校验组名算法合法
        LinkGroupDO linkGroupDO = checkLinkGroup(addRequest.getGroupId(), accountNo);

        // 长链摘要
        String originalUrlDigest = CommonUtil.MD5(addRequest.getOriginalUrl());

        // 生成短链码
        String shortLinkCode = shortLinkComponent.createShortLinkCode(addRequest.getOriginalUrl());

        // 加锁 TODO

        // 查询短链码有没有被占用，可以从数据库中找，也可以有其他方式（缓存等 待完善）
        ShortLinkDO shortLinCodeDOInDB = shortLinkManager.findByShortLinCode(shortLinkCode);


        if (shortLinCodeDOInDB == null) {
            // 处理C端
            if (EventMessageType.SHORT_LINK_ADD_LINK.name().equalsIgnoreCase(messageType)) {
                ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                        .accountNo(accountNo)
                        .code(shortLinkCode)
                        .title(addRequest.getTitle())
                        .originalUrl(addRequest.getOriginalUrl())
                        .domain(domainDO.getValue())
                        .groupId(linkGroupDO.getId())
                        .expired(addRequest.getExpired())
                        .sign(originalUrlDigest)
                        .state(ShortLinkStateEnum.ACTIVE.name())
                        .del(0)
                        .build();
                shortLinkManager.addShortLink(shortLinkDO);
                return true;

            } else if (EventMessageType.SHORT_LINK_ADD_MAPPING.name().equalsIgnoreCase(messageType)) {
                // 处理B端
                GroupCodeMappingDO groupCodeMappingDO = GroupCodeMappingDO.builder()
                        .accountNo(accountNo)
                        .code(shortLinkCode)
                        .title(addRequest.getTitle())
                        .originalUrl(addRequest.getOriginalUrl())
                        .domain(domainDO.getValue())
                        .groupId(linkGroupDO.getId())
                        .expired(addRequest.getExpired())
                        .sign(originalUrlDigest)
                        .state(ShortLinkStateEnum.ACTIVE.name())
                        .del(0)
                        .build();

                groupCodeMappingManager.add(groupCodeMappingDO);
                return true;

            }
        }

        return false;
    }

    /**
     * 校验域名
     *
     * @param domainType
     * @param domainId
     * @param accountNo
     * @return
     */
    private DomainDO checkDomain(String domainType, Long domainId, Long accountNo) {

        DomainDO domainDO;

        if (DomainTypeEnum.CUSTOM.name().equalsIgnoreCase(domainType)) {
            domainDO = domainManager.findById(domainId, accountNo);

        } else {
            domainDO = domainManager.findByDomainTypeAndID(domainId, DomainTypeEnum.OFFICIAL);
        }
        Assert.notNull(domainDO, "短链域名不合法");
        return domainDO;
    }

    /**
     * 校验组名
     *
     * @param groupId
     * @param accountNo
     * @return
     */
    private LinkGroupDO checkLinkGroup(Long groupId, Long accountNo) {

        LinkGroupDO linkGroupDO = linkGroupManager.detail(groupId, accountNo);
        Assert.notNull(linkGroupDO, "组名不合法");
        return linkGroupDO;
    }

}
