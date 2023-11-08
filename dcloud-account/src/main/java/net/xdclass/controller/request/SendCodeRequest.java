package net.xdclass.controller.request;

import lombok.Data;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/9 00:21
 */
@Data
public class SendCodeRequest {

    private String captcha;

    private String to;
}