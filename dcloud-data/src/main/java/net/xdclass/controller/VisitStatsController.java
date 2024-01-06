package net.xdclass.controller;

import net.xdclass.controller.request.VisitRecordPageRequest;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.service.VisitStatsService;
import net.xdclass.util.JsonData;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2024/1/5 00:38
 */
@RestController
@RequestMapping("/api/visit_stats/v1")
public class VisitStatsController {

    @Resource
    private VisitStatsService visitStatsService;

    @RequestMapping("page_record")
    public JsonData pageVisitRecord(@RequestBody VisitRecordPageRequest request){
        // 条数限制
        int total = request.getSize() * request.getPage();
        if(total > 1000){
            return JsonData.buildResult(BizCodeEnum.DATA_OUT_OF_LIMIT_SIZE);
        }
        Map<String,Object> pageResult = visitStatsService.pageVisitRecord(request);
        return JsonData.buildSuccess(pageResult);
    }
}