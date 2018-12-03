package com.ahfdkun.curator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class TestCurator9 {

    public static void main(String[] args) {

        final CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < Runtime.getRuntime().availableProcessors() * 4; i++) {
            new Thread(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DateFormat df = new SimpleDateFormat("HH:mm:ss|SSS");
                String orderNo = df.format(new Date());
                System.out.println("生成的订单号是：" + orderNo);
            }).start();
        }
        latch.countDown();
    }
}
