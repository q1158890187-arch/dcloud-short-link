package net.xdclass.service;

import net.xdclass.controller.request.ShortLinkAddRequest;
import net.xdclass.controller.request.ShortLinkDelRequest;
import net.xdclass.controller.request.ShortLinkPageRequest;
import net.xdclass.controller.request.ShortLinkUpdateRequest;
import net.xdclass.model.EventMessage;
import net.xdclass.util.JsonData;
import net.xdclass.vo.ShortLinkVO;

import java.util.Map;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/19 15:09
 */
public interface ShortLinkService {

    /**
     * 解析短链
     *
     * @param shortLinkCode
     * @return
     */
    ShortLinkVO parseShortLinkCode(String shortLinkCode);

    /**
     * 创建短链
     *
     * @param request
     * @return
     */
    JsonData createShortLink(ShortLinkAddRequest request);

    /**
     * 分页查找短链
     *
     * @param request
     * @return
     */
    Map<String, Object> pageByGroupId(ShortLinkPageRequest request);

    /**
     * 删除短链
     *
     * @param request
     * @return
     */
    JsonData del(ShortLinkDelRequest request);

    /**
     * 更新
     *
     * @param request
     * @return
     */
    JsonData update(ShortLinkUpdateRequest request);

    /**
     * 处理新增短链消息
     *
     * @param eventMessage
     * @return
     */
    boolean handleAddShortLink(EventMessage eventMessage);

    /**
     * 更新短链
     *
     * @param eventMessage
     * @return
     */
    boolean handleUpdateShortLink(EventMessage eventMessage);

    /**
     * 删除短链
     * @param eventMessage
     * @return
     */
    boolean handleDelShortLink(EventMessage eventMessage);

}
