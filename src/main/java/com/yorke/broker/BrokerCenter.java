package com.yorke.broker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by BG242387 on 2018/3/28.
 */
public class BrokerCenter {

    public static void main() {
        boolean isRunning = true;
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            Selector acceptSelector = Selector.open();
            ssc.register(acceptSelector, SelectionKey.OP_ACCEPT);
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(6666));
            while(isRunning) {
                acceptSelector.select();
                Set<SelectionKey> selectionKeys = acceptSelector.selectedKeys();
                Iterator<SelectionKey> iterator =  selectionKeys.iterator();
                while(iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    SocketChannel sc = ssc.accept();
                    if(sc != null) {
                        //sc.register();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
