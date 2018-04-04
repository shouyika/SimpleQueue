package com.yorke.broker;

import com.yorke.data.SimpleStringData;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by BG242387 on 2018/4/4.
 */
public class Producer {
    private static Socket socket;

    public static void main(String[] args) {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("127.0.0.1", 7777));
            SimpleStringData simpleStringData = new SimpleStringData();
            for(int i = 0; i < 100; ++i) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    //just ignore
                }
                simpleStringData.setContent("let me go~~~" + i);
                socket.getOutputStream().write(simpleStringData.toByte());
            }
            socket.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
