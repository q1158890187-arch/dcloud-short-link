package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.controller.request.VisitRecordPageRequest;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.mapper.VisitStatsMapper;
import net.xdclass.model.VisitStatsDO;
import net.xdclass.service.VisitStatsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2024/1/5 00:39
 */
@Service
@Slf4j
public class VisitStatsServiceImpl implements VisitStatsService {

    @Resource
    private VisitStatsMapper visitStatsMapper;

    @Override
    public Map<String, Object> pageVisitRecord(VisitRecordPageRequest request) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        Map<String, Object> data = new HashMap<>(16);
        String code = request.getCode();
        int page = request.getPage();
        int size = request.getSize();

        int count = visitStatsMapper.countTotal(code, accountNo);
        int from = (page - 1) * size;

        List<VisitStatsDO> list = visitStatsMapper.pageVisitRecord(code, accountNo, from, size);
        data.put("total", count);
        data.put("current_page", page);
        /*计算总页数*/
        int totalPage = 0;
        if (count % size == 0) {
            totalPage = count / size;
        } else {
            totalPage = count / size + 1;
        }
        data.put("total_page", totalPage);
        data.put("data", list);
        return data;
    }
}