package com.ahfdkun.zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class TestZookeeper4 implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void process(WatchedEvent event) {
        System.out.println("---------Receive watched event: " + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            latch.countDown();
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.1.9:2181", 100, new TestZookeeper4());
        latch.await();

        // 异步创建节点
        zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, (rc, path, ctx, name) -> {
            System.out.println("1::::::::::Create path result:[rc:" + rc + ",path:" + path + ",ctx:" + ctx + ", real path name:" + name);
        }, "abcd");

        zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, (rc, path, ctx, name) -> {
            System.out.println("2::::::::::Create path result:[rc:" + rc + ",path:" + path + ",ctx:" + ctx + ", real path name:" + name);
        }, "abcd");

        zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, (rc, path, ctx, name) -> {
            System.out.println("3::::::::::Create path result:[rc:" + rc + ",path:" + path + ",ctx:" + ctx + ", real path name:" + name);

        }, "abcd");

        Thread.sleep(Integer.MAX_VALUE);
    }
}
