package net.xdclass.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/17 17:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficPageRequest {

    private  int page;

    private int size;
}