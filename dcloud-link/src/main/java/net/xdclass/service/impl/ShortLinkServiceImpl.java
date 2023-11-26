package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.component.ShortLinkComponent;
import net.xdclass.config.RabbitMQConfig;
import net.xdclass.controller.request.ShortLinkAddRequest;
import net.xdclass.controller.request.ShortLinkDelRequest;
import net.xdclass.controller.request.ShortLinkPageRequest;
import net.xdclass.controller.request.ShortLinkUpdateRequest;
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
import net.xdclass.util.*;
import net.xdclass.vo.ShortLinkVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @Resource
    private GroupCodeMappingManager groupCodeMappingManager;

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;


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


        String newOriginalUrl = CommonUtil.addUrlPrefix(request.getOriginalUrl());
        request.setOriginalUrl(newOriginalUrl);

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
        // 短链码重复标记
        boolean duplicateCodeFlag = false;

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

        // 加锁
        // key1是短链码，ARGV[1]是accountNo,ARGV[2]是过期时间
        String script = "if redis.call('EXISTS',KEYS[1])==0 then redis.call('set',KEYS[1],ARGV[1]); redis.call('expire',KEYS[1],ARGV[2]); return 1;" +
                " elseif redis.call('get',KEYS[1]) == ARGV[1] then return 2;" +
                " else return 0; end;";

        Long result = redisTemplate.execute(new
                DefaultRedisScript<>(script, Long.class), Arrays.asList(shortLinkCode), accountNo, 100);

        // 加锁成功
        if (result > 0) {
            // C端处理
            if (EventMessageType.SHORT_LINK_ADD_LINK.name().equalsIgnoreCase(messageType)) {

                // 先判断是否短链码被占用
                ShortLinkDO shortLinCodeDOInDB = shortLinkManager.findByShortLinCode(shortLinkCode);

                if (shortLinCodeDOInDB == null) {
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
                } else {
                    log.error("C端短链码重复:{}", eventMessage);
                    duplicateCodeFlag = true;
                }

            } else if (EventMessageType.SHORT_LINK_ADD_MAPPING.name().equalsIgnoreCase(messageType)) {

                // B端处理
                GroupCodeMappingDO groupCodeMappingDOInDB = groupCodeMappingManager.findByCodeAndGroupId(shortLinkCode, linkGroupDO.getId(), accountNo);

                if (groupCodeMappingDOInDB == null) {
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
                } else {
                    log.error("B端短链码重复:{}", eventMessage);
                    duplicateCodeFlag = true;
                }
            }

        } else {
            // 加锁失败，自旋100毫秒，在调用；失败的可能是短链码已经被占用，需要重新生成
            log.error("加锁失败:{}", eventMessage);
            try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) {}
            duplicateCodeFlag = true;
        }

        if (duplicateCodeFlag) {
            String newOriginalUrl = CommonUtil.addUrlPrefixVersion(addRequest.getOriginalUrl());
            addRequest.setOriginalUrl(newOriginalUrl);
            eventMessage.setContent(JsonUtil.obj2Json(addRequest));
            log.warn("短链码报错失败，重新生成:{}", eventMessage);
            handlerAddShortLink(eventMessage);
        }
        return false;
    }

    /**
     * 从B端查找，group_code_mapping表
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> pageByGroupId(ShortLinkPageRequest request) {

        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        Map<String, Object> result = groupCodeMappingManager.pageShortLinkByGroupId(request.getPage(), request.getSize(), accountNo, request.getGroupId());

        return result;
    }

    @Override
    public JsonData del(ShortLinkDelRequest request) {

        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();


        EventMessage eventMessage = EventMessage.builder().accountNo(accountNo)
                .content(JsonUtil.obj2Json(request))
                .messageId(IDUtil.geneSnowFlakeID().toString())
                .eventMessageType(EventMessageType.SHORT_LINK_DEL.name())
                .build();

        rabbitTemplate.convertAndSend(rabbitMQConfig.getShortLinkEventExchange(), rabbitMQConfig.getShortLinkDelRoutingKey(), eventMessage);

        return JsonData.buildSuccess();

    }


    @Override
    public JsonData update(ShortLinkUpdateRequest request) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();


        EventMessage eventMessage = EventMessage.builder().accountNo(accountNo)
                .content(JsonUtil.obj2Json(request))
                .messageId(IDUtil.geneSnowFlakeID().toString())
                .eventMessageType(EventMessageType.SHORT_LINK_UPDATE.name())
                .build();

        rabbitTemplate.convertAndSend(rabbitMQConfig.getShortLinkEventExchange(), rabbitMQConfig.getShortLinkUpdateRoutingKey(), eventMessage);

        return JsonData.buildSuccess();
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
