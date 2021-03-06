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
        System.out.println("######Receive watched event: " + event);
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
        zk = new ZooKeeper("192.168.1.7:2181/test", 10000, new TestZookeeper8());
        latch.await();

        String path = "/zk-book";
        if (zk.exists(path, true) != null) {
            zk.delete(path, -1);
        }

        zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        zk.getData(path, event -> System.out.println("自定义watcher回调。。。。"), null);

        zk.getData(path, true, (rc, path12, ctx, data, stat) -> {
            System.out.println("getData添加的Watcher回调");
        }, null);

        zk.setData(path, "123".getBytes(), -1, (rc, path1, ctx, stat) -> System.out.println(rc + ", " + path1 + ", " + ctx + ", " + stat), "上下文信息");

        Thread.sleep(5000);
    }
}
