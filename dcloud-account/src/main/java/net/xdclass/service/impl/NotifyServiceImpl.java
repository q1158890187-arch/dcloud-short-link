package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.component.SmsComponent;
import net.xdclass.config.SmsConfig;
import net.xdclass.constant.RedisKey;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.SendCodeEnum;
import net.xdclass.service.NotifyService;
import net.xdclass.util.CheckUtil;
import net.xdclass.util.CommonUtil;
import net.xdclass.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/7 00:32
 */
@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {


    /**
     * 10分钟有效
     */
    private static final int CODE_EXPIRED = 60*1000*10;

    @Resource
    private SmsComponent smsComponent;

    @Resource
    private SmsConfig smsConfig;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to) {

        String cacheKey = String.format(RedisKey.CHECK_CODE_KEY,sendCodeEnum.name(),to);
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);

        //如果不为空，再判断是否是60秒内重复发送 0122_232131321314132
        if (cacheValue != null){
            long ttl = Long.parseLong(cacheValue.split("_")[1]);
            //当前时间戳-验证码发送的时间戳，如果小于60秒，则不给重复发送
            long leftTime = System.currentTimeMillis() - ttl;
            if (leftTime < (1000*60)){
                log.info("重复发送短信验证码，时间间隔:{}秒",leftTime);
                return JsonData.buildResult(BizCodeEnum.CODE_LIMITED);
            }
        }

        String code = CommonUtil.getRandomCode(6);
        // 生成拼接好的验证码
        String value = code + "_" + System.currentTimeMillis();
        redisTemplate.opsForValue().set(cacheKey,value,CODE_EXPIRED, TimeUnit.MILLISECONDS);

        if (CheckUtil.isEmail(to)){
            // TODO: 发送邮箱验证码

        } else if (CheckUtil.isPhone(to)){
            // 发送手机验证码
            smsComponent.send(to,smsConfig.getTemplateId(),code);
        }

        return JsonData.buildSuccess();
    }
}