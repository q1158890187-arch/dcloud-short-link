package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manage.DomainManager;
import net.xdclass.model.DomainDO;
import net.xdclass.service.DomainService;
import net.xdclass.vo.DomainVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/20 22:31
 */
@Service
@Slf4j
public class DomainServiceImpl implements DomainService {

    @Resource
    private DomainManager domainManager;
    @Override
    public List<DomainVO> listAll() {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        List<DomainDO> customDomainList = domainManager.listCustomDomain(accountNo);
        List<DomainDO> officialDomainList = domainManager.listOfficialDomain();
        customDomainList.addAll(officialDomainList);
        return customDomainList.stream().map(obj-> beanProcess(obj)).collect(Collectors.toList());
    }


    private DomainVO beanProcess(DomainDO domainDO){
        DomainVO domainVO = new DomainVO();
        BeanUtils.copyProperties(domainDO,domainVO);
        return domainVO;
    }
}