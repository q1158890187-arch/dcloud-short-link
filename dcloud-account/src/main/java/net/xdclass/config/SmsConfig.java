package net.xdclass.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/7 00:47
 */
@ConfigurationProperties(prefix = "sms")
@Configuration
@Data
public class SmsConfig {

    private String templateId;

    private String appCode;

}