package net.xdclass.manage;

import net.xdclass.enums.DomainTypeEnum;
import net.xdclass.model.DomainDO;

import java.util.List;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/20 22:22
 */
public interface DomainManager {
    /**
     * 查找详情
     * @param id
     * @param accountNO
     * @return
     */
    DomainDO findById(Long id, Long accountNO);


    /**
     * 查找详情
     * @param id
     * @param domainTypeEnum
     * @return
     */
    DomainDO findByDomainTypeAndID(Long id, DomainTypeEnum domainTypeEnum);


    /**
     * 新增
     * @param domainDO
     * @return
     */
    int addDomain(DomainDO domainDO);


    /**
     * 列举全部官方域名
     * @return
     */
    List<DomainDO> listOfficialDomain();


    /**
     * 列举全部自定义域名
     * @return
     */
    List<DomainDO> listCustomDomain(Long accountNo);


}
