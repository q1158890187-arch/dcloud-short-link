package net.xdclass.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/19 02:09
 */

@Component
@Slf4j
public class MyJobHandler {

    @XxlJob(value = "demoJobHandler",init = "init",destroy = "destroy")
    public ReturnT<String> execute(String param){

        log.info("小滴课堂 execute 任务方法触发成功");

        return ReturnT.SUCCESS;
    }

    private void init(){


        log.info("小滴课堂 MyJobHandler init >>>>>");
    }

    private void destroy(){
        log.info("小滴课堂 MyJobHandler destroy >>>>>");
    }

}
