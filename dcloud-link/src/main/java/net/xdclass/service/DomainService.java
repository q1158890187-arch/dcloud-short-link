package net.xdclass.service;

import net.xdclass.vo.DomainVO;

import java.util.List;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/20 22:30
 */
public interface DomainService {

    /**
     * 列举全部可用域名
     * @return
     */
    List<DomainVO> listAll();
}