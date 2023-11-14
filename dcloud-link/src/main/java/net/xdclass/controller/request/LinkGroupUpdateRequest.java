package net.xdclass.controller.request;

import lombok.Data;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/15 00:51
 */
@Data
public class LinkGroupUpdateRequest {
    /**
     * 组id
     */
    private Long id;
    /**
     * 组名
     */
    private String title;
}