package net.xdclass.controller.request;

import lombok.Data;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2024/1/6 14:34
 */
@Data
public class VisitRecordPageRequest {

    private String code;

    private int size;

    private int page;

}