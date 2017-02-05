package com.yx.listener;

import com.yx.common.RedsPubSub;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * User: LiWenC
 * Date: 17-2-5
 */
public class PubSubListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //启动系统时开启线程订阅相应频道
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Jedis jr = new Jedis("192.168.10.156", 6379, 0);
                RedsPubSub redsPubSub = new RedsPubSub();
                redsPubSub.proceed(jr.getClient(), "channel_person");
            }
        });
        thread.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
