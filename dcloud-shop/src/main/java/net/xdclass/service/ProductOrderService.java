package net.xdclass.service;

import net.xdclass.controller.request.ConfirmOrderRequest;
import net.xdclass.util.JsonData;

import java.util.Map;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/30 00:21
 */
public interface ProductOrderService {

    Map<String,Object> page(int page, int size, String state);

    String queryProductOrderState(String outTradeNo);

    JsonData confirmOrder(ConfirmOrderRequest orderRequest);
}