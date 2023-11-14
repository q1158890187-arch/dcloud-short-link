package net.xdclass.service.impl;

import net.xdclass.controller.request.LinkGroupAddRequest;
import net.xdclass.controller.request.LinkGroupUpdateRequest;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manage.LinkGroupManager;
import net.xdclass.model.LinkGroupDO;
import net.xdclass.service.LinkGroupService;
import net.xdclass.vo.LinkGroupVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/14 00:02
 */
@Service
public class LinkGroupServiceImpl implements LinkGroupService {

    @Resource
    private LinkGroupManager linkGroupManager;

    @Override
    public int add(LinkGroupAddRequest addRequest) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        LinkGroupDO linkGroupDO = new LinkGroupDO();
        linkGroupDO.setAccountNo(accountNo);
        linkGroupDO.setTitle(addRequest.getTitle());

        int rows = linkGroupManager.add(linkGroupDO);
        return rows;
    }

    @Override
    public int del(Long groupId) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        return linkGroupManager.del(groupId,accountNo);
    }

    @Override
    public LinkGroupVO detail(Long groupId) {

        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        LinkGroupDO linkGroupDO = linkGroupManager.detail(groupId, accountNo);

        LinkGroupVO linkGroupVO = new LinkGroupVO();

        // mapStruct
        BeanUtils.copyProperties(linkGroupDO, linkGroupVO);

        return linkGroupVO;
    }

    @Override
    public List<LinkGroupVO> listAllGroup() {

        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        List<LinkGroupDO> linkGroupDOList = linkGroupManager.listAllGroup(accountNo);

        List<LinkGroupVO> groupVOList = linkGroupDOList.stream().map(obj -> {

            LinkGroupVO linkGroupVO = new LinkGroupVO();
            BeanUtils.copyProperties(obj, linkGroupVO);
            return linkGroupVO;

        }).collect(Collectors.toList());


        return groupVOList;
    }



    @Override
    public int updateById(LinkGroupUpdateRequest request) {

        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        LinkGroupDO linkGroupDO = new LinkGroupDO();
        linkGroupDO.setTitle(request.getTitle());
        linkGroupDO.setId(request.getId());
        linkGroupDO.setAccountNo(accountNo);

        int rows = linkGroupManager.updateById(linkGroupDO);

        return rows;
    }
}