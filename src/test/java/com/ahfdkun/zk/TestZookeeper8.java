package com.ahfdkun.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class TestZookeeper8 implements Watcher {

    public static final AsyncCallback.DataCallback CB = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int rc, String path1, Object ctx, byte[] data, Stat stat) {
            System.out.println(rc + ", " + path1 + ", " + new String(data));
            System.out.println(stat.getCzxid() + ", " + stat.getMzxid() + ", " + stat.getVersion());
        }
    };

    private static CountDownLatch latch = new CountDownLatch(1);
    private static ZooKeeper zk = null;

    @Override
    public void process(WatchedEvent event) {
//        System.out.println("######Receive watched event: " + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && event.getPath() == null) {
                latch.countDown();
            } else if (event.getType() == Event.EventType.NodeDataChanged) {
                try {
                    zk.getData(event.getPath(), true, CB, null);
                } catch (Exception ignore) {
                }
            }
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zk = new ZooKeeper("192.168.1.9:2181", 100, new TestZookeeper8());
        latch.await();

        String path = "/zk-book";
        zk.delete(path, -1);

        zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        zk.getData(path, true, CB, null);

        zk.setData(path, "123".getBytes(), -1);

        Thread.sleep(5000);
    }
}
