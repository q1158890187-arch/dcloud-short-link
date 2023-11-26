package net.xdclass.controller.request;

import lombok.Data;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/26 22:12
 */
@Data
public class ShortLinkDelRequest {


    /**
     * 组
     */
    private Long groupId;

    /**
     * 映射id
     */
    private Long mappingId;


    /**
     * 短链码
     */
    private String code;

}
