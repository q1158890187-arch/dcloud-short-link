package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.service.NotifyService;
import net.xdclass.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/7 00:32
 */
@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {

    @Resource
    private RestTemplate restTemplate;

    @Override
    @Async("threadPoolTaskExecutor")
    public void testSend() {
        // try {
        //     TimeUnit.MILLISECONDS.sleep(200);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }

        long beginTime = CommonUtil.getCurrentTimestamp();
        // ResponseEntity<String> forEntity = restTemplate.getForEntity("https://xdclass.net/", String.class);
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://www.baidu.com/", String.class);
        String body = forEntity.getBody();
        long endTime = CommonUtil.getCurrentTimestamp();
        log.info("耗时={},body={}",endTime-beginTime,body);
    }

}