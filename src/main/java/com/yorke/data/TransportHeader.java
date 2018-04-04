package com.yorke.data;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by BG242387 on 2018/4/4.
 */
public class TransportHeader {
    public static final int BYTE_LENGTH = 8;

    private int length;

    private int type;

    public TransportHeader(byte[] headerData) {
        DataInputStream dis = new DataInputStream(new ByteInputStream(headerData, BYTE_LENGTH));
        try {
            length = dis.readInt();
            type = dis.readInt();
        } catch (IOException e) {
        }
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
