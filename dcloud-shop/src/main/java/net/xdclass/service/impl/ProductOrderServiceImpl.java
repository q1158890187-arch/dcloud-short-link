package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.controller.request.ConfirmOrderRequest;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manager.ProductOrderManager;
import net.xdclass.model.ProductOrderDO;
import net.xdclass.service.ProductOrderService;
import net.xdclass.util.JsonData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/30 00:22
 */
@Service
@Slf4j
public class ProductOrderServiceImpl implements ProductOrderService {

    @Resource
    private ProductOrderManager productOrderManager;

    @Override
    public Map<String, Object> page(int page, int size, String state) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        Map<String, Object> pageResult = productOrderManager.page(page, size, accountNo, state);
        return pageResult;
    }

    @Override
    public String queryProductOrderState(String outTradeNo) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        ProductOrderDO productOrderDO = productOrderManager.findByOutTradeNoAndAccountNo(outTradeNo, accountNo);
        if (productOrderDO == null) {
            return "null";
        } else {
            return productOrderDO.getState();
        }
    }

    @Override
    public JsonData confirmOrder(ConfirmOrderRequest orderRequest) {
        return null;
    }
}