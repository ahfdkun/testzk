package com.ahfdkun.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class TestCurator7 {

    static String path = "/zk-book";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("192.168.1.9:2181")
            .sessionTimeoutMs(15000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    public static void main(String[] args) throws Exception {
        client.start();

        PathChildrenCache cache = new PathChildrenCache(client, path, false);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener((client, event) -> {
            System.out.println("event: " + event);
        });

        client.create().forPath(path);
        Thread.sleep(1000);

        client.create().forPath(path + "/c1");
        Thread.sleep(100);

        client.delete().forPath(path + "/c1");
        Thread.sleep(100);

        client.delete().forPath(path);
        Thread.sleep(1000);

        cache.close();
        client.close();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
