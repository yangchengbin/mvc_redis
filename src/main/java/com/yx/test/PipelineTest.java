package com.yx.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.Map;

/**
 * 利用管道 大大提高了执行效率 count不是越大越好
 */
public class PipelineTest {

    public static void main(String[] args) {
        withoutPipeline();
    }

    private static void withoutPipeline() {
        Jedis jr = null;
        try {
            jr = new Jedis("115.28.100.160", 6379);
            Map<String, String> map = jr.hgetAll("1");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + "-----------" + entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jr != null) {
                jr.disconnect();
            }
        }
    }

    private static void usePipeline(int count) {
        Jedis jr = null;
        try {
            jr = new Jedis("115.28.100.160", 6379);
            Pipeline pl = jr.pipelined();
            for (int i = 0; i < count; i++) {
                pl.incr("key2");
            }
            pl.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jr != null) {
                jr.disconnect();
            }
        }
    }
}
