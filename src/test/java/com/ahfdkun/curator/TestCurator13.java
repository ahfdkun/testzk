package com.ahfdkun.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class TestCurator13 {

    static String path = "/curator_recipes_barrier_path";


    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    CuratorFramework client = CuratorFrameworkFactory.builder()
                            .connectString("192.168.1.7:2181")
                            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                            .build();
                    client.getConnectionStateListenable().addListener((client1, state) -> System.out.println(state));
                    client.start();
                    DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, path, 5);
                    Thread.sleep(Math.round(Math.random() * 3000));
                    System.out.println(Thread.currentThread().getName() + "号barrier设置 ");
                    // 设置barrier
                    barrier.enter();
                    System.err.println("启动。。。。");

                    Thread.sleep(Math.round(Math.random() * 3000));
                    barrier.leave();

                    System.out.println("退出。。。");
                } catch (Exception e) {}
            }).start();
        }
    }
}
