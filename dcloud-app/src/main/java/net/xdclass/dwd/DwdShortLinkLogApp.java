package net.xdclass.dwd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.util.DeviceUtil;
import net.xdclass.util.KafkaUtil;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.util.Map;
import java.util.TreeMap;

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
        DataStream<String> ds =  env.socketTextStream("127.0.0.1",8888);
        // FlinkKafkaConsumer<String> kafkaConsumer = KafkaUtil.getKafkaConsumer(SOURCE_TOPIC, GROUP_ID);
        // DataStreamSource<String> ds = env.addSource(kafkaConsumer);
        ds.print();
        SingleOutputStreamOperator<JSONObject> jsonDS = ds.flatMap(new FlatMapFunction<String, JSONObject>() {
            @Override
            public void flatMap(String value, Collector<JSONObject> out) throws Exception {

                JSONObject jsonObject = JSON.parseObject(value);
                //生成设备唯一id
                String udid = getDeviceId(jsonObject);
                jsonObject.put("udid",udid);
                out.collect(jsonObject);
            }
        });

        env.execute();
    }


    /**
     * 生成设备唯一id
     * @param jsonObject
     * @return
     */
    public static String getDeviceId(JSONObject jsonObject){
        Map<String,String> map = new TreeMap<>();

        try {
            map.put("ip",jsonObject.getString("ip"));
            map.put("event",jsonObject.getString("event"));
            map.put("bizId",jsonObject.getString("bizId"));
            String userAgent = jsonObject.getJSONObject("data").getString("user-agent");
            map.put("userAgent",userAgent);
            String deviceId = DeviceUtil.geneWebUniqueDeviceId(map);
            return deviceId;

        }catch (Exception e){

            log.error("生成唯一deviceid异常:{}",jsonObject);
            return null;
        }

    }

}