package net.xdclass.manage.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.ShortLinkStateEnum;
import net.xdclass.manage.GroupCodeMappingManager;
import net.xdclass.mapper.GroupCodeMappingMapper;
import net.xdclass.model.GroupCodeMappingDO;
import net.xdclass.vo.GroupCodeMappingVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/20 01:12
 */
@Component
@Slf4j
public class GroupCodeMappingManagerImpl implements GroupCodeMappingManager {

    @Resource
    private GroupCodeMappingMapper groupCodeMappingMapper;
    @Override
    public GroupCodeMappingDO findByGroupIdAndMappingId(Long mappingId, Long accountNo, Long groupId) {

        GroupCodeMappingDO groupCodeMappingDO = groupCodeMappingMapper.selectOne(new LambdaQueryWrapper<GroupCodeMappingDO>()
                .eq(GroupCodeMappingDO::getId, mappingId).eq(GroupCodeMappingDO::getAccountNo, accountNo).eq(GroupCodeMappingDO::getGroupId, groupId));
        return groupCodeMappingDO;
    }

    @Override
    public int add(GroupCodeMappingDO groupCodeMappingDO) {
        return groupCodeMappingMapper.insert(groupCodeMappingDO);
    }

    @Override
    public int del(String shortLinkCode, Long accountNo, Long groupId) {
        int rows = groupCodeMappingMapper.update(null, new LambdaUpdateWrapper<GroupCodeMappingDO>()
                .eq(GroupCodeMappingDO::getCode, shortLinkCode).eq(GroupCodeMappingDO::getAccountNo, accountNo)
                .eq(GroupCodeMappingDO::getGroupId, groupId).set(GroupCodeMappingDO::getState, 1));
        return rows;
    }

    @Override
    public Map<String, Object> pageShortLinkByGroupId(Integer page, Integer size, Long accountNo, Long groupId) {
        Page<GroupCodeMappingDO> pageInfo = new Page<>(page, size);

        Page<GroupCodeMappingDO> groupCodeMappingDOPage = groupCodeMappingMapper.selectPage(pageInfo, new LambdaQueryWrapper<GroupCodeMappingDO>().eq(GroupCodeMappingDO::getAccountNo, accountNo)
                .eq(GroupCodeMappingDO::getGroupId, groupId));

        Map<String, Object> pageMap = new HashMap<>(3);

        pageMap.put("total_record", groupCodeMappingDOPage.getTotal());
        pageMap.put("total_page", groupCodeMappingDOPage.getPages());
        pageMap.put("current_data", groupCodeMappingDOPage.getRecords()
                .stream().map(obj -> beanProcess(obj)).collect(Collectors.toList()));


        return pageMap;

    }

    @Override
    public int updateGroupCodeMappingState(Long accountNo, Long groupId, String shortLinkCode, ShortLinkStateEnum shortLinkStateEnum) {
        int rows = groupCodeMappingMapper.update(null, new LambdaUpdateWrapper<GroupCodeMappingDO>()
                .eq(GroupCodeMappingDO::getCode, shortLinkCode).eq(GroupCodeMappingDO::getAccountNo, accountNo)
                .eq(GroupCodeMappingDO::getGroupId, groupId).set(GroupCodeMappingDO::getState, shortLinkStateEnum.name()));
        return rows;
    }

    private GroupCodeMappingVO beanProcess(GroupCodeMappingDO groupCodeMappingDO) {
        GroupCodeMappingVO groupCodeMappingVO = new GroupCodeMappingVO();
        BeanUtils.copyProperties(groupCodeMappingDO, groupCodeMappingVO);

        return groupCodeMappingVO;
    }
}