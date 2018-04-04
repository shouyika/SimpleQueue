package com.yorke.data;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

/**
 * Created by BG242387 on 2018/4/4.
 */
public class ProcessContext {
    private TransportHeader header;

    private Object body;

    private ByteOutputStream buffer = new ByteOutputStream(1024);

    public TransportHeader getHeader() {
        return header;
    }

    public void setHeader(TransportHeader header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public ByteOutputStream getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteOutputStream buffer) {
        this.buffer = buffer;
    }
}
