package net.xdclass.dwd;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.util.KafkaUtil;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/12/26 01:10
 */
@Slf4j
public class DwdShortLinkLogApp {

    /**
     * 定义topic
     */
    public static final String SOURCE_TOPIC = "ods_link_visit_topic";

    /**
     * 定义消费者组
     */
    public static final String GROUP_ID = "dwd_short_link_group";

    public static void main(String [] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // DataStream<String> ds =  env.socketTextStream("127.0.0.1",8888);
        FlinkKafkaConsumer<String> kafkaConsumer = KafkaUtil.getKafkaConsumer(SOURCE_TOPIC, GROUP_ID);
        DataStreamSource<String> ds = env.addSource(kafkaConsumer);
        ds.print();
        env.execute();
    }

}