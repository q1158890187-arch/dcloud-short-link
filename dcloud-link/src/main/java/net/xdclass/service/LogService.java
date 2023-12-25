package net.xdclass.service;

import net.xdclass.util.JsonData;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/26 00:31
 */
public interface LogService {

    JsonData recordShortLinkLog(String msg);

}