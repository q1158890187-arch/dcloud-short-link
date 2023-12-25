package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.service.LogService;
import net.xdclass.util.JsonData;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/26 00:31
 */
@Service
@Slf4j
public class LogServiceImpl implements LogService {
    @Override
    public JsonData recordShortLinkLog(String msg) {
        log.info("这个是记录日志:{}",msg);
        return JsonData.buildSuccess();
    }
}