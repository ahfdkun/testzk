package com.ahfdkun.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class TestCurator4 {

    static String path = "/zk-book/c1";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("192.168.1.7:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    public static void main(String[] args) throws Exception {
        client.start();

        String s = client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path, "init".getBytes());

        System.out.println(s);

        Stat stat = new Stat();
        byte[] bytes = client.getData().storingStatIn(stat)
                .forPath(path);
        System.out.println(new String(bytes));

        client.setData().withVersion(stat.getVersion()).forPath(path);

        client.setData().withVersion(stat.getVersion()).forPath(path);

        Thread.sleep(2000);
    }
}
