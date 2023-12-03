package net.xdclass.controller.request;

import lombok.Data;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/3 15:34
 */
@Data
public class ProductOrderPageRequest {


    /**
     * 状态
     */
    private String state;

    /**
     * 第几页
     */
    private int page;

    /**
     * 每页多少条
     */
    private int size;

}
