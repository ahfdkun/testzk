package com.ahfdkun.zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class TestZookeeper11 implements Watcher {

    private static ZooKeeper zk = null;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String path = "/zk-book-auth_test";
        String path2 = "/zk-book-auth_test/c1";

        ZooKeeper zk1 = new ZooKeeper("192.168.1.9:2181", 50000, new TestZookeeper11());
        zk1.addAuthInfo("digest", "foo:true".getBytes());
        // 对于删除操作，其权限作用于子节点，而非当前节点
        zk1.create(path, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        zk1.create(path2, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

        ZooKeeper zk2 = new ZooKeeper("192.168.1.9:2181", 50000, new TestZookeeper11());
        zk2.addAuthInfo("digest", "foo:true".getBytes());
        // 需要权限
        zk2.delete(path2, -1);

        ZooKeeper zk3 = new ZooKeeper("192.168.1.9:2181", 50000, new TestZookeeper11());
        // 不需要权限
        zk3.delete(path, -1);

        Thread.sleep(5000);
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("######Receive watched event: " + event);
    }
}
