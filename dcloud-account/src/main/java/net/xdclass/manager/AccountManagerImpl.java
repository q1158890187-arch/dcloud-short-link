package net.xdclass.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.manager.impl.AccountManager;
import net.xdclass.mapper.AccountMapper;
import net.xdclass.model.AccountDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/5 23:51
 */
@Component
@Slf4j
public class AccountManagerImpl implements AccountManager {

    @Resource
    private AccountMapper accountMapper;

    @Override
    public int insert(AccountDO accountDO) {
        return accountMapper.insert(accountDO);
    }

    @Override
    public List<AccountDO> findByPhone(String phone) {
        List<AccountDO> accountDOList = accountMapper.selectList(new LambdaQueryWrapper<AccountDO>().eq(AccountDO::getPhone, phone));
        return accountDOList;
    }
}