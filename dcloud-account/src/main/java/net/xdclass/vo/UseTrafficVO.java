package net.xdclass.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.xdclass.model.TrafficDO;

import java.util.List;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/20 01:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UseTrafficVO {


    /**
     * 天剩余可用总次数 = 总次数 - 已用
     */
    private Integer dayTotalLeftTimes;


    /**
     * 当前使用的流量包
     */
    private TrafficDO currentTrafficDO;


    /**
     * 记录没过期，但是今天没更新的流量包id
     */
    private List<Long> unUpdatedTrafficIds;


}
