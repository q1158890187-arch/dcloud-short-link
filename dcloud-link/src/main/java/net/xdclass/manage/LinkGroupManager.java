package net.xdclass.manage;

import net.xdclass.model.LinkGroupDO;

import java.util.List;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/14 23:16
 */
public interface LinkGroupManager {

    int add(LinkGroupDO linkGroupDO);

    int del(Long groupId, Long accountNo);

    LinkGroupDO detail(Long groupId, Long accountNo);

    List<LinkGroupDO> listAllGroup(Long accountNo);

    int updateById(LinkGroupDO linkGroupDO);
}