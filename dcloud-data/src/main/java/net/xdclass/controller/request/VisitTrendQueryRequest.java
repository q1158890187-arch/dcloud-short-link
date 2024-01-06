package net.xdclass.controller.request;

import lombok.Data;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2024/1/6 15:09
 */
@Data
public class VisitTrendQueryRequest {

    private String code;
    /**
     * 跨天、当天24小时、分钟级别
     */
    private String type;

    private String startTime;

    private String endTime;

}