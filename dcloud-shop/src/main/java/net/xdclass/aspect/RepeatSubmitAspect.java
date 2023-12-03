package net.xdclass.aspect;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.annotation.RepeatSubmit;
import net.xdclass.constant.RedisKey;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.exception.BizException;
import net.xdclass.interceptor.LoginInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 定义一个切面类
 * @author: zhengqinghua
 * @date: 2023/12/2 21:06
 */
@Aspect
@Component
@Slf4j
public class RepeatSubmitAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 定义 @Pointcut注解表达式，
     * 方式一：@annotation：当执行的方法上拥有指定的注解时生效（我们采用这）
     * 方式二：execution：一般用于指定方法的执行
     */
    @Pointcut("@annotation(repeatSubmit)")
    public void pointCutNoRepeatSubmit(RepeatSubmit repeatSubmit){

    }

    /**
     * 环绕通知, 围绕着方法执行
     *
     * @param joinPoint
     * @param repeatSubmit
     * @return
     * @throws Throwable
     * @Around 可以用来在调用一个具体方法前和调用后来完成一些具体的任务。
     * <p>
     * 方式一：单用 @Around("execution(* net.xdclass.controller.*.*(..))")可以
     * 方式二：用@Pointcut和@Around联合注解也可以（我们采用这个）
     * <p>
     * <p>
     * 两种方式
     * 方式一：加锁 固定时间内不能重复提交
     * <p>
     * 方式二：先请求获取token，这边再删除token,删除成功则是第一次提交
     */
    @Around("pointCutNoRepeatSubmit(repeatSubmit)")
    public Object around(ProceedingJoinPoint joinPoint, RepeatSubmit repeatSubmit) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        //用于记录成功或者失败
        boolean res = false;


        //防重提交类型
        String type = repeatSubmit.limitType().name();
        if(type.equalsIgnoreCase(RepeatSubmit.Type.PARAM.name())){
            //方式一，参数形式防重提交 TODO

        }else {
            //方式二，令牌形式防重提交
            String requestToken = request.getHeader("request-token");
            if(StringUtils.isBlank(requestToken)){
                throw new BizException(BizCodeEnum.ORDER_CONFIRM_TOKEN_EQUAL_FAIL);
            }

            String key = String.format(RedisKey.SUBMIT_ORDER_TOKEN_KEY,accountNo,requestToken);

            /**
             * 提交表单的token key
             * 方式一：不用lua脚本获取再判断，之前是因为 key组成是 order:submit:accountNo, value是对应的token，所以需要先获取值，再判断
             * 方式二：可以直接key是 order:submit:accountNo:token,然后直接删除成功则完成
             */
            res = redisTemplate.delete(key);

        }
        if(!res){
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_REPEAT);
        }


        log.info("环绕通知执行前");

        Object obj = joinPoint.proceed();

        log.info("环绕通知执行后");

        return obj;

    }


}