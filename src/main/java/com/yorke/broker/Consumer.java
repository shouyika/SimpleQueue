package com.yorke.broker;

import com.yorke.data.ProcessContext;
import com.yorke.data.TransportHeader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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
        sc.register(selector, SelectionKey.OP_READ, new ProcessContext());
        counter.incrementAndGet();
    }

    private class ReaderTask implements Runnable {
        volatile boolean isRunning;

        ReaderProcessor readerProcessor;

        ReaderTask() {
            this.isRunning = true;
            readerProcessor = new ReaderProcessor();
        }

        public void run() {
            try {
                while (isRunning) {
                    selector.select(20);
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        readerProcessor.process(key);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
                //todo error handler
            }
        }

        void stop() {
            isRunning = false;
        }
    }

    private class ReaderProcessor {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        void process(SelectionKey key) {
            SocketChannel sc = (SocketChannel) key.channel();
            ProcessContext context = (ProcessContext) key.attachment();
            try {
                byteBuffer.clear();
                int ret;
                while ((ret = sc.read(byteBuffer)) > 0) {
                    byteBuffer.flip();
                    int len = byteBuffer.limit();
                    context.getBuffer().write(byteBuffer.array(), 0, len);
                    byteBuffer.clear();
                }
                if (context.getBuffer().size() >= TransportHeader.BYTE_LENGTH) {
                    context.setHeader(new TransportHeader(context.getBuffer().getBytes()));
                }
                else {
                    return;
                }
                if (context.getBuffer().size() >= context.getHeader().getLength()) {
                    int oldSize = context.getBuffer().size();
                    byte[] content = context.getBuffer().getBytes();
                    System.out
                            .println(new String(content, TransportHeader.BYTE_LENGTH,
                                    context.getHeader().getLength() - TransportHeader.BYTE_LENGTH, "UTF-8"));
                    context.getBuffer().reset();
                    context.getBuffer()
                            .write(content, context.getHeader().getLength(), oldSize - context.getHeader().getLength());
                }
                if (ret == -1) {
                    sc.close();
                    key.cancel();
                    counter.decrementAndGet();
                }
            } catch (IOException e) {
                e.printStackTrace();
                key.cancel();
                counter.decrementAndGet();
            }
        }
    }
}
