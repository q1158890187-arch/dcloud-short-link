package net.xdclass.manager;

import net.xdclass.model.AccountDO;

import java.util.List;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/5 23:52
 */
public interface AccountManager {

    int insert(AccountDO accountDO);

    List<AccountDO> findByPhone(String phone);
}