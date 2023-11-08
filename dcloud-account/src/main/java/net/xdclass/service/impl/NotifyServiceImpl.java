package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.component.SmsComponent;
import net.xdclass.config.SmsConfig;
import net.xdclass.enums.SendCodeEnum;
import net.xdclass.service.NotifyService;
import net.xdclass.util.CheckUtil;
import net.xdclass.util.CommonUtil;
import net.xdclass.util.JsonData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/7 00:32
 */
@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {

    @Resource
    private SmsComponent smsComponent;

    @Resource
    private SmsConfig smsConfig;

    @Resource
    private RestTemplate restTemplate;

    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to) {

        String code = CommonUtil.getRandomCode(6);

        if (CheckUtil.isEmail(to)){
            // TODO: 发送邮箱验证码

        } else if (CheckUtil.isPhone(to)){
            // 发送手机验证码
            smsComponent.send(to,smsConfig.getTemplateId(),code);
        }

        return JsonData.buildSuccess();
    }
}