package com.ahfdkun.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.ZooKeeper;

public class TestCurator14 {

    static String path = "/curator_zkpath_sample";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("192.168.1.9:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    public static void main(String[] args) throws Exception {
        client.start();

        ZooKeeper zooKeeper = client.getZookeeperClient().getZooKeeper();
        System.out.println(ZKPaths.fixForNamespace(path, "sub"));
        System.out.println(ZKPaths.makePath(path, "sub"));
        System.out.println(ZKPaths.getNodeFromPath(path + "/sub1"));

        ZKPaths.PathAndNode pathAndNode = ZKPaths.getPathAndNode(path + "/sub1");
        System.out.println(pathAndNode.getNode());
        System.out.println(pathAndNode.getPath());

        ZKPaths.mkdirs(zooKeeper, path + "/child1");
        ZKPaths.mkdirs(zooKeeper, path + "/child2");

        System.out.println(ZKPaths.getSortedChildren(zooKeeper, path));
        ZKPaths.deleteChildren(zooKeeper, path, true);

    }
}
