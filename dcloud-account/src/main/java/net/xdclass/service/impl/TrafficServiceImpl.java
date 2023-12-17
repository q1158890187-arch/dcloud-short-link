package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.EventMessageType;
import net.xdclass.manager.TrafficManager;
import net.xdclass.model.EventMessage;
import net.xdclass.model.TrafficDO;
import net.xdclass.service.TrafficService;
import net.xdclass.util.JsonUtil;
import net.xdclass.vo.ProductVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/17 16:37
 */
@Service
@Slf4j
public class TrafficServiceImpl implements TrafficService {

    @Resource
    private TrafficManager trafficManager;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void handleTrafficMessage(EventMessage eventMessage) {

        String messageType = eventMessage.getEventMessageType();
        if (EventMessageType.PRODUCT_ORDER_PAY.name().equalsIgnoreCase(messageType)) {

            // 订单已经支付，新增流量

            String content = eventMessage.getContent();
            Map<String, Object> orderInfoMap = JsonUtil.json2Obj(content, Map.class);

            // 还原订单商品信息
            Long accountNo = (Long) orderInfoMap.get("accountNo");
            String outTradeNo = (String) orderInfoMap.get("outTradeNo");
            Integer buyNum = (Integer) orderInfoMap.get("buyNum");
            String productStr = (String) orderInfoMap.get("product");
            ProductVO productVO = JsonUtil.json2Obj(productStr, ProductVO.class);
            log.info("商品信息:{}", productVO);

            // 流量包有效期
            LocalDateTime expiredDateTime = LocalDateTime.now().plusDays(productVO.getValidDay());
            Date date = Date.from(expiredDateTime.atZone(ZoneId.systemDefault()).toInstant());

            // 构建流量包对象
            TrafficDO trafficDO = TrafficDO.builder()
                    .accountNo(accountNo)
                    .dayLimit(productVO.getDayTimes() * buyNum)
                    .dayUsed(0)
                    .totalLimit(productVO.getTotalTimes())
                    .pluginType(productVO.getPluginType())
                    .level(productVO.getLevel())
                    .productId(productVO.getId())
                    .outTradeNo(outTradeNo)
                    .expiredDate(date).build();

            int rows = trafficManager.add(trafficDO);
            log.info("消费消息新增流量包:rows={},trafficDO={}", rows, trafficDO);
        }
    }

}