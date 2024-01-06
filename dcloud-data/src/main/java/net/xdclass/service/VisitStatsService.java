package net.xdclass.service;

import net.xdclass.controller.request.VisitRecordPageRequest;

import java.util.Map;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2024/1/5 00:39
 */
public interface VisitStatsService {

    Map<String, Object> pageVisitRecord(VisitRecordPageRequest request);
}