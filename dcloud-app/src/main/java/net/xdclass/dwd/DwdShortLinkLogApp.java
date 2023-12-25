package net.xdclass.dwd;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/26 01:10
 */
@Slf4j
public class DwdShortLinkLogApp {

    public static void main(String [] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStream<String> ds =  env.socketTextStream("127.0.0.1",8888);
        ds.print();
        env.execute();
    }

}