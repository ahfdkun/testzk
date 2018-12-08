package com.ahfdkun.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestCurator5 {

    static String path = "/zk-book/c1";

    static CountDownLatch latch = new CountDownLatch(2);

    static ExecutorService executor = Executors.newFixedThreadPool(2);

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("192.168.1.7:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    public static void main(String[] args) throws Exception {
        client.start();

        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println("event[" + curatorEvent + "]");
                        System.out.println("Thread: " + Thread.currentThread());
                        latch.countDown();
                    }
                }, executor)
                .forPath(path, "init".getBytes());

        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println("event[" + curatorEvent + "]");
                        System.out.println("Thread: " + Thread.currentThread());
                        latch.countDown();
                    }
                })
                .forPath(path, "init".getBytes());

        latch.await();

        executor.shutdown();

        Thread.sleep(2000);
    }
}
