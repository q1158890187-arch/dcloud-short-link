package net.xdclass.constant;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/9 00:38
 */
public class RedisKey {

    /**
     * 验证码缓存key，第一个是类型,第二个是唯一标识比如手机号或者邮箱
     */
    public static final String CHECK_CODE_KEY = "code:%s:%s";

}
