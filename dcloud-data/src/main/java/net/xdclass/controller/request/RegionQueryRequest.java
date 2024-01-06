package net.xdclass.controller.request;

import lombok.Data;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2024/1/6 15:00
 */
@Data
public class RegionQueryRequest {

    private String code;

    private String startTime;

    private String endTime;
}