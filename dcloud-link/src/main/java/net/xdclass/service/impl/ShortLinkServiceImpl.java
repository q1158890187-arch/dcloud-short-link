package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.config.RabbitMQConfig;
import net.xdclass.controller.request.ShortLinkAddRequest;
import net.xdclass.enums.EventMessageType;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manage.ShortLinkManager;
import net.xdclass.model.EventMessage;
import net.xdclass.model.ShortLinkDO;
import net.xdclass.service.ShortLinkService;
import net.xdclass.util.JsonData;
import net.xdclass.util.JsonUtil;
import net.xdclass.vo.ShortLinkVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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


    @Override
    public ShortLinkVO parseShortLinkCode(String shortLinkCode) {

        ShortLinkDO shortLinkDO = shortLinkManager.findByShortLinCode(shortLinkCode);
        if(shortLinkDO == null){
            return null;
        }
        ShortLinkVO shortLinkVO = new ShortLinkVO();
        BeanUtils.copyProperties(shortLinkDO,shortLinkVO);
        return shortLinkVO;
    }

    @Override
    public JsonData createShortLink(ShortLinkAddRequest request) {

        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        EventMessage eventMessage = EventMessage.builder().accountNo(accountNo)
                .content(JsonUtil.obj2Json(request))
                .eventMessageType(EventMessageType.SHORT_LINK_ADD.name())
                .build();

        rabbitTemplate.convertAndSend(rabbitMQConfig.getShortLinkEventExchange(),rabbitMQConfig.getShortLinkAddRoutingKey(),eventMessage);
        return JsonData.buildSuccess();
    }
}
