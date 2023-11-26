package net.xdclass.manage.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.manage.ShortLinkManager;
import net.xdclass.mapper.ShortLinkMapper;
import net.xdclass.model.ShortLinkDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/18 21:41
 */
@Component
@Slf4j
public class ShortLinkManagerImpl implements ShortLinkManager {

    @Autowired
    private ShortLinkMapper shortLinkMapper;

    @Override
    public int addShortLink(ShortLinkDO shortLinkDO) {
        return shortLinkMapper.insert(shortLinkDO);
    }

    @Override
    public ShortLinkDO findByShortLinCode(String shortLinkCode) {

        ShortLinkDO shortLinkDO = shortLinkMapper.selectOne(
                new QueryWrapper<ShortLinkDO>().eq("code", shortLinkCode).eq("del", 0));
        return shortLinkDO;
    }

    @Override
    public int del(ShortLinkDO shortLinkDO) {

        int rows = shortLinkMapper.update(null,
                new UpdateWrapper<ShortLinkDO>()
                        .eq("code", shortLinkDO.getCode())
                        .eq("account_no", shortLinkDO.getAccountNo())
                        .set("del", 1));
        return rows;
    }

    @Override
    public int update(ShortLinkDO shortLinkDO) {
        int rows = shortLinkMapper.update(null, new UpdateWrapper<ShortLinkDO>()
                .eq("code", shortLinkDO.getCode())
                .eq("del", 0)
                .eq("account_no",shortLinkDO.getAccountNo())
                .set("title", shortLinkDO.getTitle())
                .set("domain", shortLinkDO.getDomain()));
        return rows;
    }
}
