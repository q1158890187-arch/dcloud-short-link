package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.LogTypeEnum;
import net.xdclass.model.LogRecord;
import net.xdclass.service.LogService;
import net.xdclass.util.CommonUtil;
import net.xdclass.util.JsonData;
import net.xdclass.util.JsonUtil;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/26 00:31
 */
@Service
@Slf4j
public class LogServiceImpl implements LogService {

    private static final String TOPIC_NAME = "ods_link_visit_topic";
    @Resource
    private KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public void recordShortLinkLog(HttpServletRequest request, String shortLinkCode, Long accountNo) {

        //ip、浏览器信息
        String ip = CommonUtil.getIpAddr(request);

        //全部请求头
        Map<String,String> headerMap = CommonUtil.getAllRequestHeader(request);

        Map<String,String> availableMap = new HashMap<>();
        availableMap.put("user-agent",headerMap.get("user-agent"));
        availableMap.put("referer",headerMap.get("referer"));
        availableMap.put("accountNo",accountNo.toString());

        LogRecord logRecord = LogRecord.builder()
                //日志类型
                .event(LogTypeEnum.SHORT_LINK_TYPE.name())
                //日志内容
                .data(availableMap)
                //客户端ip
                .ip(ip)
                //产生时间
                .ts(CommonUtil.getCurrentTimestamp())
                //业务唯一标识
                .bizId(shortLinkCode).build();


        String jsonLog = JsonUtil.obj2Json(logRecord);

        //打印控制台
        log.info(jsonLog);

        //发送kafka
        kafkaTemplate.send(TOPIC_NAME,jsonLog);
    }
}