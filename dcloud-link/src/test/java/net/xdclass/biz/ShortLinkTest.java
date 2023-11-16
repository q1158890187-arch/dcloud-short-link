package net.xdclass.biz;

import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.LinkApplication;
import net.xdclass.component.ShortLinkComponent;
import net.xdclass.strategy.ShardingDBConfig;
import net.xdclass.util.CommonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/13 23:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LinkApplication.class)
@Slf4j
public class ShortLinkTest {

    @Resource
    private ShortLinkComponent shortLinkComponent;

    @Test
    public void testMurmurHash(){

        for(int i=0; i<5; i++){

            String originalUrl = "https://xdclass.net?id="+ CommonUtil.generateUUID()+"pwd="+CommonUtil.getStringNumRandom(7);

            long murmur3_32 = Hashing.murmur3_32().hashUnencodedChars(originalUrl).padToLong();
            log.info("murmur3_32={}",murmur3_32);

        }
    }

    /**
     * 测试短链平台
     */
    @Test
    public void testCreateShortLink() {

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int num1 = random.nextInt(10);
            int num2 = random.nextInt(10000000);
            int num3 = random.nextInt(10000000);
            String originalUrl = num1 + "xdclass" + num2 + ".net" + num3;
            String shortLinkCode = shortLinkComponent.createShortLinkCode(originalUrl);
            log.info("originalUrl:" + originalUrl + ", shortLinkCode=" + shortLinkCode);
        }
    }


    @Test
    public void testRandomDB(){


        for(int i=0;i<20;i++){
            log.info(ShardingDBConfig.getRandomDBPrefix());
        }


    }


}
