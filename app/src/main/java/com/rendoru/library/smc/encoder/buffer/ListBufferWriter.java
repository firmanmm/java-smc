package com.rendoru.library.smc.encoder.buffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListBufferWriter implements IBufferWriter {

    public ByteBuffer buffer;

    public ListBufferWriter() {
        buffer = ByteBuffer.allocate(128);
    }

    @Override
    public void write(byte data) {
        allocationCheck(1);
        buffer.put(data);
    }

    @Override
    public void write(byte[] data) {
        allocationCheck(data.length);
        buffer.put(data);
    }

    @Override
    public void write(byte[] data, int start, int end) {
        allocationCheck(end - start);
        for(int i=start;i<end;i++){
            buffer.put(data[i]);
        }
    }

    private void allocationCheck(long size){
        if(buffer.remaining() < size) {
            buffer = ByteBuffer.allocate(buffer.capacity() * 2);
        }
    }

    @Override
    public byte[] getArrayCopy() {
        int size = buffer.position();
        byte[] copy = new byte[size];
        System.arraycopy(buffer.array(), 0, copy, 0, size);
        return copy;
    }
}
