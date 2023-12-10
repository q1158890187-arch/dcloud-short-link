package net.xdclass.biz;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.ShopApplication;
import net.xdclass.config.PayBeanConfig;
import net.xdclass.config.WechatPayApi;
import net.xdclass.config.WechatPayConfig;
import net.xdclass.util.CommonUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/10 00:21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApplication.class)

@Slf4j
public class WechatPayTest {

    @Autowired
    private PayBeanConfig payBeanConfig;

    @Autowired
    private WechatPayConfig payConfig;

    @Autowired
    private CloseableHttpClient wechatPayClient;


    @Test
    public void testLoadPrivateKey() throws IOException {

        log.info(payBeanConfig.getPrivateKey().getAlgorithm());

    }


    /**
     * 快速验证统一下单接口
     * @throws IOException
     */
    @Test
    public void testNativeOrder() throws IOException {

        String outTradeNo = CommonUtil.getStringNumRandom(32);

        /**
         * {
         * 	"mchid": "1900006XXX",
         * 	"out_trade_no": "native12177525012014070332333",
         * 	"appid": "wxdace645e0bc2cXXX",
         * 	"description": "Image形象店-深圳腾大-QQ公仔",
         * 	"notify_url": "https://weixin.qq.com/",
         * 	"amount": {
         * 		"total": 1,
         * 		"currency": "CNY"
         *        }
         * }
         */
        JSONObject payObj = new JSONObject();
        payObj.put("mchid",payConfig.getMchId());
        payObj.put("out_trade_no",outTradeNo);
        payObj.put("appid",payConfig.getWxPayAppid());
        payObj.put("description","小滴课堂海量数据项目大课");
        payObj.put("notify_url",payConfig.getCallbackUrl());

        //订单总金额，单位为分。
        JSONObject amountObj = new JSONObject();
        amountObj.put("total",100);
        amountObj.put("currency","CNY");

        payObj.put("amount",amountObj);
        //附属参数，可以用在回调
        payObj.put("attach","{\"accountNo\":"+888+"}");


        String body = payObj.toJSONString();

        log.info("请求参数:{}",body);

        StringEntity entity = new StringEntity(body,"utf-8");
        entity.setContentType("application/json");

        HttpPost httpPost = new HttpPost(WechatPayApi.NATIVE_ORDER);
        httpPost.setHeader("Accept","application/json");
        httpPost.setEntity(entity);

        try(CloseableHttpResponse response = wechatPayClient.execute(httpPost)){

            //响应码
            int statusCode = response.getStatusLine().getStatusCode();
            //响应体
            String responseStr = EntityUtils.toString(response.getEntity());

            log.info("下单响应码:{},响应体:{}",statusCode,responseStr);

        }catch (Exception e){
            e.printStackTrace();
        }



    }

}
