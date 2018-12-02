package com.ahfdkun.zkclient;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

@Slf4j
public class TestZkClient4 {

    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = new ZkClient("192.168.1.9:2181", 5000);
        String path = "/zk-book";

        System.out.println("exists: " + zkClient.exists(path));

        Object data = zkClient.readData(path, true);
        System.out.println("data: " + data);

        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("handleDataChange::::: dataPath: " + dataPath + ", data: " + data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("handleDataDeleted::::: dataPath: " + dataPath );
            }
        });

        zkClient.createEphemeral(path);
        zkClient.writeData(path, 123);
        zkClient.delete(path);


        Thread.sleep(2000);
        zkClient.close();
    }
}
