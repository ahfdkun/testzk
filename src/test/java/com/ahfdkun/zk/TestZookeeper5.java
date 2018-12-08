package com.ahfdkun.zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class TestZookeeper5 implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);
    private static ZooKeeper zk = null;

    @Override
    public void process(WatchedEvent event) {
        System.out.println("============ Receive watched event: " + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && event.getPath() == null) {
                latch.countDown();
            } else if (event.getType() == Event.EventType.NodeChildrenChanged) {
                try {
                    System.out.println("ReGet child: " + zk.getChildren(event.getPath(), true));
                } catch (Exception ignore) {}
            }
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zk = new ZooKeeper("192.168.1.7:2181", 100, new TestZookeeper5());
        latch.await();

        String path = "/zk-book";
        zk.delete(path + "/c2", -1);
        zk.delete(path, -1);

        zk.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        List<String> children = zk.getChildren(path, true);
        System.out.println("======== children: " + children);

        zk.create(path + "/c2", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        List<String> children2 = zk.getChildren(path + "/c2", true);
        System.out.println("======== children2: " + children2);

        zk.create(path + "/c2/aa", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        Thread.sleep(5000);
    }
}
