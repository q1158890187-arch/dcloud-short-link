package net.xdclass.service;

import net.xdclass.enums.SendCodeEnum;
import net.xdclass.util.JsonData;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/7 00:31
 */
public interface NotifyService {

    /**
     * 发送短信验证码
     * @param sendCodeEnum
     * @param to
     * @return
     */
    JsonData sendCode(SendCodeEnum sendCodeEnum, String to);

    /**
     * 校验验证码
     * @param sendCodeEnum
     * @param phone
     * @param code
     * @return
     */
    boolean checkCode(SendCodeEnum sendCodeEnum, String phone, String code);
}