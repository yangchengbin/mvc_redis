package com.yx.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 利用管道 大大提高了执行效率 count不是越大越好
 */
public class PipelineTest {

    Jedis jr;

    @Before
    public void init() {
        jr = new Jedis("115.28.100.160", 6379);
    }

    @Test
    public void testGetAll() {
        try {
            Map<String, String> map = jr.hgetAll("person");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + "-----------" + entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPipeline() {
        try {
            Pipeline pl = jr.pipelined();
            for (int i = 0; i < 100; i++) {
                pl.incr("key2");
            }
            pl.sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testZrByScoreWithScores() {//存在临界值时用此方法
        try {
            int count = 5;
            Set<Tuple> set = jr.zrangeByScoreWithScores("names", 0, 6, 0, count);
            if (set.size() == 0) {
                return;
            } else if (set.size() == count) {
                List<Tuple> list = new ArrayList<Tuple>(set);
                double tmp = list.get(list.size() - 1).getScore();

                Set<Tuple> setMore = jr.zrangeByScoreWithScores("names", tmp, tmp);
                jr.zremrangeByScore("names",0,tmp);
                set.addAll(setMore);
            }
            for (Tuple t : set) {
                System.out.println(t.getScore() + "---------" + t.getElement());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void after() {
        jr.disconnect();
    }
}
