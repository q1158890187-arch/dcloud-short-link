package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.controller.request.FrequentSourceRequset;
import net.xdclass.controller.request.RegionQueryRequest;
import net.xdclass.controller.request.VisitRecordPageRequest;
import net.xdclass.controller.request.VisitTrendQueryRequest;
import net.xdclass.enums.DateTimeFieldEnum;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.mapper.VisitStatsMapper;
import net.xdclass.model.VisitStatsDO;
import net.xdclass.service.VisitStatsService;
import net.xdclass.vo.VisitStatsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public List<VisitStatsVO> queryRegionWithDay(RegionQueryRequest request) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        List<VisitStatsDO> list = visitStatsMapper.queryRegionVisitStatsWithDay(request.getCode(), accountNo, request.getStartTime(), request.getEndTime());
        List<VisitStatsVO> visitStatsVOS = list.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());
        return visitStatsVOS;
    }

    @Override
    public List<VisitStatsVO> queryVisitTrend(VisitTrendQueryRequest request) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        String code = request.getCode();
        String type = request.getType();
        String startTime = request.getStartTime();
        String endTime = request.getEndTime();
        List<VisitStatsDO> list = null;

        if (DateTimeFieldEnum.DAY.name().equalsIgnoreCase(type)) {
            list = visitStatsMapper.queryVisitTrendWithMultiDay(code, accountNo, startTime, endTime);
        } else if (DateTimeFieldEnum.HOUR.name().equalsIgnoreCase(type)) {

        } else if (DateTimeFieldEnum.MIUNTE.name().equalsIgnoreCase(type)) {

        }
        List<VisitStatsVO> visitStatsVOS = list.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());
        return visitStatsVOS;
    }

    @Override
    public List<VisitStatsVO> queryFrequentSource(FrequentSourceRequset request) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        String code = request.getCode();
        String startTime = request.getStartTime();
        String endTime = request.getEndTime();
        List<VisitStatsDO> list = visitStatsMapper.queryFrequentSource(code, accountNo, startTime, endTime, 10);
        List<VisitStatsVO> visitStatsVOS = list.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());
        return visitStatsVOS;
    }

    /**
     * map-struct
     *
     * @param visitStatsDO
     * @return
     */
    private VisitStatsVO beanProcess(VisitStatsDO visitStatsDO) {
        VisitStatsVO visitStatsVO = new VisitStatsVO();
        BeanUtils.copyProperties(visitStatsDO, visitStatsVO);
        return visitStatsVO;
    }
}