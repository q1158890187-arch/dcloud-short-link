package net.xdclass.controller;

import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.service.NotifyService;
import net.xdclass.util.JsonData;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/7 00:30
 */
@RestController
@RequestMapping("/api/account/v1")
@Slf4j
public class NotifyController {

    @Resource
    private Producer captchaProducer;

    @Resource
    private NotifyService notifyService;

    private StringRedisTemplate stringRedisTemplate;

    private RedisTemplate redisTemplate;


    /**
     * 生成验证码
     *
     * @param request
     * @param response
     */
    @GetMapping("captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        String captchaText = captchaProducer.createText();
        log.info("验证码内容:{}", captchaText);
        // 存储redis,配置过期时间 TODO， jedis/lettuce

        BufferedImage bufferedImage = captchaProducer.createImage(captchaText);

        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error("获取流出错:{}", e.getMessage());
        }

    }

    /**
     * 测试发送验证码接口-主要是用于对比优化前后区别
     *
     * @return
     */
    @GetMapping("/send")
    public JsonData send() {

        return JsonData.buildSuccess();
    }
}