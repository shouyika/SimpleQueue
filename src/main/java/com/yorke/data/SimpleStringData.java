package com.yorke.data;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by BG242387 on 2018/4/4.
 */
public class SimpleStringData {
    private static final int STRING_TYPE = 301;

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] toByte() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            byte[] contentByte = content.getBytes("UTF-8");
            dos.writeInt(contentByte.length + TransportHeader.BYTE_LENGTH);
            dos.writeInt(STRING_TYPE);
            dos.write(contentByte);
        } catch (IOException e) {
            //may not happen
        }
        return baos.toByteArray();
    }
}
