package net.xdclass.controller;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.service.NotifyService;
import net.xdclass.util.CommonUtil;
import net.xdclass.util.JsonData;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/7 00:30
 */
@RestController
@RequestMapping("/api/account/v1")
@Slf4j
public class NotifyController {

    @Resource
    private NotifyService notifyService;

    /**
     * 测试发送验证码接口-主要是用于对比优化前后区别
     * @return
     */
    @GetMapping("/send")
    public JsonData send(){

        return JsonData.buildSuccess();
    }
}