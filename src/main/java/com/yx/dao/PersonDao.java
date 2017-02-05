package com.yx.dao;

import com.yx.bean.Person;
import org.apache.commons.beanutils.BeanUtils;
import org.msgpack.MessagePack;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class PersonDao {

    @Resource
    private StringRedisTemplate redsTemplate;

    protected static final MessagePack msgPack = new MessagePack();

    public static final byte[] CHANNEL_PERSON = "channel_person".getBytes();

    public boolean addPerson(final Person person) {
        boolean result = redsTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    RedisSerializer<String> serializer = redsTemplate.getStringSerializer();
                    byte[] key = serializer.serialize(String.valueOf(person.getId()));
                    Map<String, String> properties = BeanUtils.describe(person);
                    Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
                    for (Map.Entry<String, String> property : properties.entrySet()) {
                        if (!"class".equals(property.getKey())) {
                            map.put(serializer.serialize(property.getKey()), serializer.serialize(property.getValue()));
                        }
                    }
                    connection.hMSet(key, map);
                    //connection.expire(key, 30);//设置有效时间（秒）超过时间自动删除 默认永久 30秒
                    //connection.expireAt(key, System.currentTimeMillis() / 1000 + 30);//设置有效时间（秒）超过时间自动删除 默认永久30秒
                    connection.publish(CHANNEL_PERSON, "add person".getBytes());//发布订阅消息
                } catch (Exception e) {
                    return false;
                }
                return true;
            }
        });
        return result;
    }

    /**
     * 根据key获取对象
     */
    public Person getPersonById(final int keyId) {
        Person result = redsTemplate.execute(new RedisCallback<Person>() {
            public Person doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    RedisSerializer<String> serializer = redsTemplate.getStringSerializer();
                    byte[] key = serializer.serialize(String.valueOf(keyId));
                    Map<byte[], byte[]> map = connection.hGetAll(key);
                    Person person = new Person(keyId);
                    for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
                        String k = serializer.deserialize(entry.getKey());
                        String v = serializer.deserialize(entry.getValue());
                        BeanUtils.setProperty(person, k, v);
                    }
                    return person;
                } catch (Exception e) {
                    return null;
                }
            }
        });
        return result;
    }

    /**
     * 删除对象 ,依赖key
     */
    public void delete(String key) {
        redsTemplate.delete(key);
    }

    /**
     * 修改对象
     */
    public boolean update(final Person person) {
        if (!exists(person.getId())) {
            throw new NullPointerException("数据行不存在!");
        }
        boolean result = redsTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redsTemplate.getStringSerializer();
                byte[] key = serializer.serialize(String.valueOf(person.getId()));
                byte[] name = serializer.serialize(person.getName());
                byte[] address = serializer.serialize(person.getAddress());
                Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
                map.put("name".getBytes(), name);
                map.put("address".getBytes(), address);
                connection.hMSet(key, map);
                return true;
            }
        });
        return result;
    }

    public Boolean exists(final int key) {
        return redsTemplate.hasKey(String.valueOf(key));
    }

    public boolean testMsgPack() {
        Boolean result = redsTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    RedisSerializer<String> serializer = redsTemplate.getStringSerializer();
                    byte[] key = serializer.serialize(String.valueOf("person"));
                    Map<byte[], byte[]> map = connection.hGetAll(key);
                    Person p = new Person();
                    for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
                        String k = serializer.deserialize(entry.getKey());
                        String v = serializer.deserialize(entry.getValue());
                        BeanUtils.setProperty(p, k, v);
                    }
                    System.out.println(p);
                    return true;
                } catch (Exception e) {
                    return null;
                }
            }
        });
        return result;
    }
}
