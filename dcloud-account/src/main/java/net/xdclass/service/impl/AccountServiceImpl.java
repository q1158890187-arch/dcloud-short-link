package net.xdclass.service.impl;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import net.model.LoginUser;
import net.xdclass.controller.request.AccountLoginRequest;
import net.xdclass.controller.request.AccountRegisterRequest;
import net.xdclass.enums.AuthTypeEnum;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.SendCodeEnum;
import net.xdclass.manager.impl.AccountManager;
import net.xdclass.model.AccountDO;
import net.xdclass.service.AccountService;
import net.xdclass.service.NotifyService;
import net.xdclass.util.CommonUtil;
import net.xdclass.util.JWTUtil;
import net.xdclass.util.JsonData;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhengqinghua
 * @since 2023-11-05
 */
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Resource
    private NotifyService notifyService;

    @Resource
    private AccountManager accountManager;

    /**
     * 手机验证码验证
     * 密码加密（TODO）
     * 账号唯一性检查(TODO)
     * 插入数据库
     * 新注册用户福利发放(TODO)
     *
     * @param registerRequest
     * @return
     */
    @Override
    public JsonData register(AccountRegisterRequest registerRequest) {
        boolean checkCode = false;
        // 判断验证码是否正确
        if (StringUtils.isNotBlank(registerRequest.getPhone())) {
            checkCode = notifyService.checkCode(SendCodeEnum.USER_REGISTER, registerRequest.getPhone(), registerRequest.getCode());
        }
        // 验证码不正确
        if (!checkCode) {
            return JsonData.buildResult(BizCodeEnum.CODE_ERROR);
        }

        AccountDO accountDO = new AccountDO();
        BeanUtils.copyProperties(registerRequest, accountDO);
        // 认证级别
        accountDO.setAuth(AuthTypeEnum.DEFAULT.name());
        // 唯一账号 TODO
        accountDO.setAccountNo(CommonUtil.getCurrentTimestamp());
        // 密码加密 秘钥 盐
        accountDO.setSecret("$1$" + CommonUtil.getStringNumRandom(8));
        String cryptPwd = Md5Crypt.md5Crypt(registerRequest.getPwd().getBytes(), accountDO.getSecret());
        accountDO.setPwd(cryptPwd);

        int rows = accountManager.insert(accountDO);
        log.info("rows:{},注册成功:{}", rows, accountDO);

        // 用户注册成功，发放福利 TODO
        userRegisterInitTask(accountDO);

        return JsonData.buildSuccess();
    }

    /**
     * 1、根据手机号去找
     * 2、有的话，则用秘钥+用户传递的明文密码，进行加密，再和数据库的密文进行匹配
     *
     * @param accountLoginRequest
     * @return
     */
    @Override
    public JsonData login(AccountLoginRequest accountLoginRequest) {
        List<AccountDO> accountDOList = accountManager.findByPhone(accountLoginRequest.getPhone());
        if (!CollUtil.isEmpty(accountDOList) && accountDOList.size() == 1) {
            AccountDO accountDO = accountDOList.get(0);
            String md5Crypt = Md5Crypt.md5Crypt(accountLoginRequest.getPwd().getBytes(), accountDO.getSecret());
            if (md5Crypt.equals(accountDO.getPwd())) {
                LoginUser loginUser = LoginUser.builder().build();
                BeanUtils.copyProperties(accountDO, loginUser);
                // 生成TOKEN
                String token = JWTUtil.geneJsonWebToken(loginUser);
                return JsonData.buildSuccess(token);
            } else {
                return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
            }
        } else {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_UNREGISTER);
        }

    }

    /**
     * 用户注册成功，发放福利 TODO
     *
     * @param accountDO
     */
    private void userRegisterInitTask(AccountDO accountDO) {

    }
}
