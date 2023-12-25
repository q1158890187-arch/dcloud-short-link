package net.xdclass.service;

import net.xdclass.util.JsonData;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/26 00:31
 */
public interface LogService {

    /**
     * 记录日志
     * @param request
     * @param shortLinkCode
     * @param accountNo
     * @return
     */
    void recordShortLinkLog(HttpServletRequest request, String shortLinkCode, Long accountNo);

}