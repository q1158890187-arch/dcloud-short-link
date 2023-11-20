package net.xdclass.manage.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.DomainTypeEnum;
import net.xdclass.manage.DomainManager;
import net.xdclass.mapper.DomainMapper;
import net.xdclass.model.DomainDO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/20 22:24
 */
@Component
@Slf4j
public class DomainManagerImpl implements DomainManager {

    @Resource
    private DomainMapper domainMapper;

    @Override
    public DomainDO findById(Long id, Long accountNO) {
        return domainMapper.selectOne(new LambdaQueryWrapper<DomainDO>().eq(DomainDO::getId, id).eq(DomainDO::getAccountNo, accountNO));
    }

    @Override
    public DomainDO findByDomainTypeAndID(Long id, DomainTypeEnum domainTypeEnum) {
        return domainMapper.selectOne(new LambdaQueryWrapper<DomainDO>().eq(DomainDO::getId, id).eq(DomainDO::getDomainType,domainTypeEnum.name()));
    }

    @Override
    public int addDomain(DomainDO domainDO) {
        return domainMapper.insert(domainDO);
    }

    @Override
    public List<DomainDO> listOfficialDomain() {
        return domainMapper.selectList(new LambdaQueryWrapper<DomainDO>().eq(DomainDO::getDomainType, DomainTypeEnum.OFFICIAL.name()));
    }

    @Override
    public List<DomainDO> listCustomDomain(Long accountNo) {
        return domainMapper.selectList(new LambdaQueryWrapper<DomainDO>().eq(DomainDO::getAccountNo, accountNo).eq(DomainDO::getDomainType, DomainTypeEnum.CUSTOM.name()));
    }
}