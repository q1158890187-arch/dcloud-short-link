package net.xdclass.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/9 15:48
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "pay.wechat")
public class WechatPayConfig {

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 公众号id 需要和商户号绑定
     */
    private String wxPayAppid;
    /**
     * 商户证书序列号,需要和证书对应
     */
    private String mchSerialNo;
    /**
     * API V3密钥
     */
    private String apiV3Key;
    /**
     * 商户私钥路径（微信服务端会根据证书序列号，找到证书获取公钥进行解密数据）
     */
    private String privateKeyPath;
    /**
     * 支付成功页面跳转
     */
    private String successReturnUrl;

    /**
     * 支付成功，回调通知
     */
    private String callbackUrl;


    public static class Url{

        /**
         * native下单接口
         */
        public static final String NATIVE_ORDER = "https://api.mch.weixin.qq.com/v3/pay/transactions/native";
        public static final String NATIVE_ORDER_PATH = "/v3/pay/transactions/native";


        /**
         * native订单查询接口，根据商户订单号查询
         * 一个是根据微信订单号，一个是根据商户订单号
         */
        public static final String NATIVE_QUERY = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/%s?mchid=%s";
        public static final String NATIVE_QUERY_PATH = "/v3/pay/transactions/out-trade-no/%s?mchid=%s";


        /**
         * native关闭订单接口
         */
        public static final String NATIVE_CLOSE = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/%s/close";
        public static final String NATIVE_CLOSE_PATH = "/v3/pay/transactions/out-trade-no/%s/close";

    }



}
