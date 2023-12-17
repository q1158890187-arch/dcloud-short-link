package net.xdclass.service;

import net.xdclass.controller.request.TrafficPageRequest;
import net.xdclass.model.EventMessage;
import net.xdclass.vo.TrafficVO;

import java.util.Map;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/17 16:37
 */
public interface TrafficService {

    void handleTrafficMessage(EventMessage eventMessage);

    Map<String,Object> pageAvailable(TrafficPageRequest request);

    TrafficVO detail(long trafficId);
}