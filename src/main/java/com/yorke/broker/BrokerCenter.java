package com.yorke.broker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class BrokerCenter {

    public static void main(String[] args) {
        boolean isRunning = true;
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            Selector acceptSelector = Selector.open();
            ssc.configureBlocking(false);
            ssc.register(acceptSelector, SelectionKey.OP_ACCEPT);
            Consumer consumer = new Consumer();
            consumer.start();
            ssc.bind(new InetSocketAddress(7777));
            while (isRunning) {
                acceptSelector.select();
                Set<SelectionKey> selectionKeys = acceptSelector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    SocketChannel sc = ssc.accept();
                    if (sc != null) {
                        consumer.add(sc);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
