package net.xdclass.config;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/10 01:24
 */
public class WechatPayApi {


    /**
     * 微信支付主机地址
     */
    public static final String HOST = "https://api.mch.weixin.qq.com";


    /**
     * Native下单
     */
    public static final String NATIVE_ORDER = HOST+ "/v3/pay/transactions/native";

    /**
     * Native订单状态查询, 根据商户订单号查询
     */
    public static final String NATIVE_QUERY = HOST+ "/v3/pay/transactions/out-trade-no/%s?mchid=%s";


    /**
     * 关闭订单接口
     */
    public static final String NATIVE_CLOSE_ORDER = HOST+ "/v3/pay/transactions/out-trade-no/%s/close";

    /**
     * 申请退款接口
     */
    public static final String NATIVE_REFUND_ORDER = HOST+ "/v3/refund/domestic/refunds";

}
