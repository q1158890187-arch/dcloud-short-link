package net.xdclass.service;


import net.xdclass.controller.request.AccountRegisterRequest;
import net.xdclass.util.JsonData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhengqinghua
 * @since 2023-11-05
 */
public interface AccountService {

    /**
     * 用户注册
     * @param registerRequest
     * @return
     */
    JsonData register(AccountRegisterRequest registerRequest);
}
