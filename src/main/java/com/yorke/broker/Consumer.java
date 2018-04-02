package com.yorke.broker;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by BG242387 on 2018/3/28.
 */
public class Consumer {
    private Selector selector;

    private AtomicInteger counter;

    public Consumer() throws IOException {
        counter = new AtomicInteger();
        selector = Selector.open();
    }

    public void start() {
        new Thread(new ReaderTask()).start();
    }

    public void add(SocketChannel sc) throws IOException {
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
        counter.incrementAndGet();
    }

    private class ReaderTask implements Runnable {
        boolean isRunning;

        ReaderTask() {
            this.isRunning = true;
        }

        @Override
        public void run() {
            try {
                while (isRunning) {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        new ReaderProcessor(key).process();
                    }
                }
            } catch (Throwable e) {
                //todo error handler
            }
        }

        void stop() {
            isRunning = false;
        }
    }

    private class ReaderProcessor {
        SelectionKey key;

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        ReaderProcessor(SelectionKey key) {
            SocketChannel ss;
            this.key = key;
        }

        void process() {
            SocketChannel sc = (SocketChannel) key.channel();
            try {
                while (sc.read(byteBuffer) != 0) {
//                    sc.
//                    System.out.println();
                }
            } catch (IOException e) {
                key.cancel();
            }
        }
    }
}
