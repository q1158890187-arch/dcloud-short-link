package net.xdclass.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/22 01:42
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UseTrafficRequest {


    /**
     * 账号
     */
    private Long accountNo;

    /**
     * 业务id, 短链码
     */
    private String bizId;
}
