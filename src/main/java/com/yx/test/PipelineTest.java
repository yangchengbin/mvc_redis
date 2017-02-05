package com.yx.test;

import com.yx.bean.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.msgpack.MessagePack;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 利用管道 大大提高了执行效率 count不是越大越好
 */
public class PipelineTest {

    Jedis jr;
    List<Person> persons = new ArrayList<Person>();
    MessagePack messagePack = new MessagePack();
    Map<byte[], byte[]> map;

    @Before
    public void init() {
        jr = new Jedis("192.168.10.156", 6379);
        for (int i = 0; i < 100000; i++) {
            persons.add(new Person(i, "name" + i, "address" + i));
        }
    }

    @Test
    public void testPipeline() {
        try {
            Pipeline pl = jr.pipelined();
            for (int i = 0; i < persons.size(); i++) {
                pl.set(messagePack.write("person" + i), messagePack.write(persons.get(i)));
            }
            pl.sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPipeline2() {
        try {
            Pipeline pl = jr.pipelined();
            for (int i = 0; i < persons.size(); i++) {
                map = new HashMap<byte[], byte[]>();
                map.put("id".getBytes(), String.valueOf(persons.get(i).getId()).getBytes());
                map.put("name".getBytes(), persons.get(i).getName().getBytes());
                map.put("address".getBytes(), persons.get(i).getAddress().getBytes());
                pl.hmset(messagePack.write("person" + i), map);
            }
            pl.sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void after() {
        jr.disconnect();
    }
}
