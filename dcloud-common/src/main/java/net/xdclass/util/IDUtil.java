package net.xdclass.util;

import org.apache.shardingsphere.core.strategy.keygen.SnowflakeShardingKeyGenerator;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/13 00:29
 */
public class IDUtil {


    private static SnowflakeShardingKeyGenerator shardingKeyGenerator = new SnowflakeShardingKeyGenerator();

    /**
     * 雪花算法生成器
     * @return
     */
    public static Comparable<?> geneSnowFlakeID(){

        return shardingKeyGenerator.generateKey();
    }

}
