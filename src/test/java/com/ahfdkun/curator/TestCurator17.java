package com.ahfdkun.curator;

import org.apache.curator.test.TestingCluster;
import org.apache.curator.test.TestingZooKeeperServer;

public class TestCurator17 {

    public static void main(String[] args) throws Exception {
        TestingCluster cluster = new TestingCluster(3);
        cluster.start();
        Thread.sleep(2000);

        TestingZooKeeperServer leader = null;
        for (TestingZooKeeperServer zs : cluster.getServers()) {
            System.out.print(zs.getInstanceSpec().getServerId() + "-");
            System.out.print(zs.getQuorumPeer().getServerState() + "-");
            System.out.println(zs.getInstanceSpec().getDataDirectory().getAbsolutePath());
            if (zs.getQuorumPeer().getServerState().equals("leading")) {
                leader = zs;
            }
        }
        leader.kill();

        Thread.sleep(2000);

        System.out.println("--After kill: ");
        for (TestingZooKeeperServer zs : cluster.getServers()) {
            System.out.print(zs.getInstanceSpec().getServerId() + "-");
            System.out.print(zs.getQuorumPeer().getServerState() + "-");
            System.out.println(zs.getInstanceSpec().getDataDirectory().getAbsolutePath());
        }
        cluster.stop();
    }
}
