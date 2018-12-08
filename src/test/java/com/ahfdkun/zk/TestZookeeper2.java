package com.ahfdkun.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class TestZookeeper2 implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void process(WatchedEvent event) {
        System.out.println("---------Receive watched event: " + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            latch.countDown();
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.1.7:2181", 100, new TestZookeeper2());
        latch.await();
        long sessionId = zooKeeper.getSessionId();
        byte[] sessionPasswd = zooKeeper.getSessionPasswd();
        System.out.println("---------sessionPasswd: " + Arrays.toString(sessionPasswd));

        // Use illegal sessionId and sessionPasswd
        zooKeeper = new ZooKeeper("192.168.1.7:2181", 100, new TestZookeeper2(), 1l, "test".getBytes());

        // Use correct sessionId and sessionPasswd
        zooKeeper = new ZooKeeper("192.168.1.7:2181", 100, new TestZookeeper2(), sessionId, sessionPasswd);
        Thread.sleep(Integer.MAX_VALUE);

    }
}
