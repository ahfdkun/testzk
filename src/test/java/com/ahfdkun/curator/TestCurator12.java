package com.ahfdkun.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

public class TestCurator12 {

    static String path = "/curator_recipes_barrier_path";

    static DistributedBarrier barrier;

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    CuratorFramework client = CuratorFrameworkFactory.builder()
                            .connectString("192.168.1.9:2181")
                            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                            .build();
                    client.start();
                    barrier = new DistributedBarrier(client, path);
                    System.out.println(Thread.currentThread().getName() + "号barrier设置 ");
                    // 设置barrier
                    barrier.setBarrier();
                    // 等待barrier释放
                    barrier.waitOnBarrier();
                    System.err.println("启动。。。。");
                } catch (Exception e) {}
            }).start();
        }
        Thread.sleep(1000);
        barrier.removeBarrier();
    }
}
