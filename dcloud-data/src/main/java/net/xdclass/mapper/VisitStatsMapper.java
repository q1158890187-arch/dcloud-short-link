package net.xdclass.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.xdclass.model.VisitStatsDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2024/1/5 00:38
 */
public interface VisitStatsMapper extends BaseMapper<VisitStatsDO> {

    /**
     * 计算总条数
     * @param code
     * @param accountNo
     * @return
     */
    int countTotal(@Param("code") String code, @Param("accountNo") Long accountNo);

    /**
     * 分页查询
     * @param code
     * @param accountNo
     * @param from
     * @param size
     * @return
     */
    List<VisitStatsDO> pageVisitRecord(@Param("code") String code, @Param("accountNo") Long accountNo,
                                       @Param("from") int from, @Param("size") int size);
}