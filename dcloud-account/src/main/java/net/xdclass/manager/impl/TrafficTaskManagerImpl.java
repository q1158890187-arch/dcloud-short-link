package net.xdclass.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.manager.TrafficTaskManager;
import net.xdclass.mapper.TrafficTaskMapper;
import net.xdclass.model.TrafficTaskDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/23 16:37
 */
@Service
@Slf4j
public class TrafficTaskManagerImpl implements TrafficTaskManager {

    @Resource
    private TrafficTaskMapper trafficTaskMapper;
    @Override
    public int add(TrafficTaskDO trafficTaskDO) {
        return trafficTaskMapper.insert(trafficTaskDO);
    }

    @Override
    public TrafficTaskDO findByIdAndAccountNo(Long id, Long accountNo) {
        TrafficTaskDO trafficTaskDO = trafficTaskMapper.selectOne(new LambdaQueryWrapper<TrafficTaskDO>()
                .eq(TrafficTaskDO::getId, id).eq(TrafficTaskDO::getAccountNo, accountNo));
        return trafficTaskDO;
    }

    @Override
    public int deleteByIdAndAccountNo(Long id, Long accountNo) {
        return trafficTaskMapper.delete(new LambdaQueryWrapper<TrafficTaskDO>()
                .eq(TrafficTaskDO::getId, id).eq(TrafficTaskDO::getAccountNo, accountNo));
    }
}