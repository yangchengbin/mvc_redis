package com.yx.listener;

import redis.clients.jedis.Jedis;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

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
                Jedis jr = new Jedis("192.168.1.125", 6379, 0);
                jr.auth("root");

              /*  RedsPubSub redsPubSub = new RedsPubSub();
                redsPubSub.proceed(jr.getClient(), "channel_person");*/
                //key失效监听机制
                RedsMsgPubSubListener listener = new RedsMsgPubSubListener();
                jr.subscribe(listener, "__keyevent@0__:expired");
                //key失效监听机制
            }
        });
        thread.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
