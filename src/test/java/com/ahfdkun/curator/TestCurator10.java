package com.ahfdkun.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class TestCurator10 {

    static String lock_path = "/curator_recipes_lock_path";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("192.168.1.9:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    public static void main(String[] args) throws Exception {
        client.start();

        final InterProcessMutex lock = new InterProcessMutex(client, lock_path);

        final CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < Runtime.getRuntime().availableProcessors() * 100; i++) {
            new Thread(() -> {
                try {
                    latch.await();
                    lock.acquire();
                } catch (Exception e) {}
                DateFormat df = new SimpleDateFormat("HH:mm:ss|SSS");
                String orderNo = df.format(new Date());
                System.out.println("生成的订单号是：" + orderNo);
                try {
                    lock.release();
                } catch (Exception e) {}
            }).start();
        }
        latch.countDown();
    }
}
