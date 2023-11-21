package net.xdclass.service;

import net.xdclass.controller.request.ShortLinkAddRequest;
import net.xdclass.util.JsonData;
import net.xdclass.vo.ShortLinkVO;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/19 15:09
 */
public interface ShortLinkService {

    /**
     * 解析短链
     * @param shortLinkCode
     * @return
     */
    ShortLinkVO parseShortLinkCode(String shortLinkCode);

    /**
     * 创建短链
     * @param request
     * @return
     */
    JsonData createShortLink(ShortLinkAddRequest request);
}
