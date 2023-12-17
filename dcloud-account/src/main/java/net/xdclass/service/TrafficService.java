package net.xdclass.service;

import net.xdclass.model.EventMessage;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/17 16:37
 */
public interface TrafficService {

    void handleTrafficMessage(EventMessage eventMessage);
}