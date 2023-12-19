package net.xdclass.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.service.TrafficService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/19 02:09
 */

@Component
@Slf4j
public class TrafficJobHandler {

    @Resource
    private TrafficService trafficService;

    /**
     * 过期流量包处理
     * @param param
     * @return
     */
    @XxlJob(value = "trafficExpiredHandler",init = "init",destroy = "destroy")
    public ReturnT<String> execute(String param){

        log.info("小滴课堂 execute 任务方法触发成功,删除过期流量包");
        trafficService.deleteExpireTraffic();
        return ReturnT.SUCCESS;
    }

    private void init(){
        log.info("小滴课堂 MyJobHandler init >>>>>");
    }

    private void destroy(){
        log.info("小滴课堂 MyJobHandler destroy >>>>>");
    }

}
