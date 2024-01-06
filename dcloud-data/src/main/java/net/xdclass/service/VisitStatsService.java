package net.xdclass.service;

import net.xdclass.controller.request.RegionQueryRequest;
import net.xdclass.controller.request.VisitRecordPageRequest;
import net.xdclass.vo.VisitStatsVO;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2024/1/5 00:39
 */
public interface VisitStatsService {

    Map<String, Object> pageVisitRecord(VisitRecordPageRequest request);

    List<VisitStatsVO> queryRegionWithDay(RegionQueryRequest request);
}