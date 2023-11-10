package net.xdclass.controller.request;

import lombok.Data;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/10 01:09
 */
@Data
public class AccountLoginRequest {

    private String phone;

    private String pwd;
}