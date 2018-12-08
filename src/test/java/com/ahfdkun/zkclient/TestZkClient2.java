package com.ahfdkun.zkclient;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

@Slf4j
public class TestZkClient2 {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("192.168.1.7:2181", 5000);
        String path = "/zk-book/c1";
        zkClient.createPersistent(path, true);

        // 递归删除
        zkClient.deleteRecursive(path + "/c2");
    }
}
