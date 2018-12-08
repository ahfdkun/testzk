package com.ahfdkun.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class TestZookeeper7 implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);
    private static ZooKeeper zk = null;
    private static Stat stat = new Stat();

    @Override
    public void process(WatchedEvent event) {
//        System.out.println("######Receive watched event: " + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && event.getPath() == null) {
                latch.countDown();
            } else if (event.getType() == Event.EventType.NodeDataChanged) {
                try {
                    System.out.println("data2:" + new String(zk.getData(event.getPath(), true, stat)));
                    System.out.println("stat2 getCzxid:" + stat.getCzxid() + ",getMzxid：" + stat.getMzxid() + ",getCtime：" + stat.getCtime() + ",getMtime:" + stat.getMtime());
                } catch (Exception ignore) {
                }
            }
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zk = new ZooKeeper("192.168.1.7:2181", 100, new TestZookeeper7());
        latch.await();

        String path = "/zk-book";
        zk.delete(path, -1);

        zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        System.out.println("data1:" + new String(zk.getData(path, true, stat)));
        System.out.println("stat1 getCzxid:" + stat.getCzxid() + ",getMzxid：" + stat.getMzxid() + ",getCtime：" + stat.getCtime() + ",getMtime:" + stat.getMtime());
        zk.setData(path, "123".getBytes(), stat.getVersion());

        Thread.sleep(5000);
    }
}
