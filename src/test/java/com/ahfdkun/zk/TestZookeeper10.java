package com.ahfdkun.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class TestZookeeper10 implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);
    private static ZooKeeper zk = null;

    @Override
    public void process(WatchedEvent event) {
        System.out.println("######Receive watched event: " + event);
        try {
            if (Event.KeeperState.SyncConnected == event.getState()) {
                if (Event.EventType.None == event.getType() && event.getPath() == null) {
                    latch.countDown();
                } else if (Event.EventType.NodeCreated == event.getType()) {
                    System.out.println("Node(" + event.getPath() + ")Created!");
                    zk.exists(event.getPath(), true);
                } else if (Event.EventType.NodeDeleted == event.getType()) {
                    System.out.println("Node(" + event.getPath() + ")Deleted!");
                    zk.exists(event.getPath(), true);
                } else if (Event.EventType.NodeDataChanged == event.getType()) {
                    System.out.println("Node(" + event.getPath() + ")DataChanged!");
                    zk.exists(event.getPath(), true);
                }
            }
        } catch (Exception ignore) {}
    }


    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zk = new ZooKeeper("192.168.1.7:2181", 100, new TestZookeeper10());
        latch.await();

        String path = "/zk-book";
//        zk.delete(path, -1);

        Stat exists = zk.exists(path, true);
        System.out.println(exists);

        zk.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.setData(path, "123".getBytes(), -1);

        zk.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        zk.delete(path + "/c1", -1);
        zk.delete(path, -1);

        Thread.sleep(5000);
    }
}
