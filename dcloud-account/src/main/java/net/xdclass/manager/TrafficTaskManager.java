package net.xdclass.manager;

import net.xdclass.model.TrafficTaskDO;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/23 16:36
 */
public interface TrafficTaskManager {

    int add(TrafficTaskDO trafficTaskDO);

    TrafficTaskDO findByIdAndAccountNo(Long id,Long accountNo);

    int deleteByIdAndAccountNo(Long id,Long accountNo);

}