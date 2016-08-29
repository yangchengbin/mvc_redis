package com.yx.test;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Tuple;

import java.util.Map;
import java.util.Set;

/**
 * 利用管道 大大提高了执行效率 count不是越大越好
 */
public class PipelineTest {

    public static void main(String[] args) {
    }

    @Test
    public void withoutPipeline() {
        Jedis jr = null;
        try {
            jr = new Jedis("115.28.100.160", 6379);
            Map<String, String> map = jr.hgetAll("person");
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

    public void usePipeline() {
        Jedis jr = null;
        try {
            jr = new Jedis("115.28.100.160", 6379);
            Pipeline pl = jr.pipelined();
            for (int i = 0; i < 100; i++) {
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

    @Test
    public void testRange() {
        Jedis jr = null;
        try {
            jr = new Jedis("115.28.100.160", 6379);
            long  start = System.currentTimeMillis();
            Set<Tuple> set = jr.zrangeByScoreWithScores("names", 110, 111);//没有值也不为null size=0
            System.out.println(System.currentTimeMillis() - start);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jr != null) {
                jr.disconnect();
            }
        }
    }
}
