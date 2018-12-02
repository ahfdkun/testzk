package com.ahfdkun.zkclient;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

@Slf4j
public class TestZkClient1 {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("192.168.1.9:2181", 5000);
        log.error("输出:{}", zkClient);
    }
}
