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
}
