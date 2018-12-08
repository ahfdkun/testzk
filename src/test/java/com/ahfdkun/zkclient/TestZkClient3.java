package com.ahfdkun.zkclient;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

@Slf4j
public class TestZkClient3 {

    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = new ZkClient("192.168.1.7:2181", 5000);
        String path = "/zk-book";


        zkClient.subscribeChildChanges(path, (parentPath, currentChilds) -> System.out.println("parentPath: " + parentPath + ", currentChilds:" + currentChilds));

        zkClient.createPersistent(path, true);
//        Thread.sleep(1000);

        System.out.println(zkClient.getChildren(path));
//        Thread.sleep(1000);

        zkClient.createPersistent(path + "/c1", true);
//        Thread.sleep(1000);

        zkClient.delete(path + "/c1");
//        Thread.sleep(1000);

        zkClient.delete(path);
//        Thread.sleep(2000);

    }
}
