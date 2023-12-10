package net.xdclass.component;

import net.xdclass.vo.PayInfoVO;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/10 19:05
 */
public interface PayStrategy {

    /**
     * 统一下单接口
     * @param payInfoVO
     * @return
     */
    String unifiedOrder(PayInfoVO payInfoVO);


    /**
     * 退款接口
     * @param payInfoVO
     * @return
     */
    default String refund(PayInfoVO payInfoVO){ return ""; }


    /**
     * 查询支付状态
     * @param payInfoVO
     * @return
     */
    default String queryPayStatus(PayInfoVO payInfoVO){ return ""; }


    /**
     * 关闭订单
     * @param payInfoVO
     * @return
     */
    default String closeOrder(PayInfoVO payInfoVO){ return ""; }

}
