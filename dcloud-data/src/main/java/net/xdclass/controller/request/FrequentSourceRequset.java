package net.xdclass.controller.request;

import lombok.Data;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2024/1/6 15:23
 */
@Data
public class FrequentSourceRequset {
    private String code;

    private String startTime;

    private String endTime;
}