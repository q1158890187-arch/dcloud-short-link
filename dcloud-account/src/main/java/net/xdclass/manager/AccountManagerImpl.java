package net.xdclass.manager;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.manager.impl.AccountManager;
import net.xdclass.mapper.AccountMapper;
import net.xdclass.model.AccountDO;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/5 23:51
 */
@Component
@Slf4j
public class AccountManagerImpl implements AccountManager {

    private AccountMapper accountMapper;

    @Override
    public int insert(AccountDO accountDO) {
        return accountMapper.insert(accountDO);
    }
}