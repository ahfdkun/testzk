package com.ahfdkun.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;

import java.io.File;

public class TestCurator16 {

    static String path = "/zookeeper";

    public static void main(String[] args) throws Exception {
        TestingServer server = new TestingServer(2181, new File("C:/zk-book-data"));
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(server.getConnectString())
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        System.out.println(client.getChildren().forPath(path));

        server.close();
    }
}
