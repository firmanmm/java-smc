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
        int length  = end - start;
        int position = buffer.position();
        allocationCheck(length);
        System.arraycopy(data, start, buffer.array(), position, length);
        buffer.position(position + length);
    }

    private void allocationCheck(long size){
        int remaining = buffer.remaining();
        if(remaining < size) {
            int oldSize = buffer.capacity();
            int position = buffer.position();
            int targetSize = (int) (size + position);
            int newSize = oldSize * 2;
            while(newSize < targetSize) {
                newSize *= 2;
            }
            ByteBuffer newBuffer = ByteBuffer.allocate(newSize);
            System.arraycopy(buffer.array(), 0, newBuffer.array(), 0, oldSize);
            buffer = newBuffer;
            buffer.position(position);
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
