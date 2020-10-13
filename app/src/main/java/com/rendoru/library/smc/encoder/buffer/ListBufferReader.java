package com.rendoru.library.smc.encoder.buffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListBufferReader implements IBufferReader {

    private int start, end;
    private byte[] data;

    public ListBufferReader(byte[] data) {
        this(data, 0, data.length);
    }

    public ListBufferReader(byte[] data, int start) {
        this(data, start, data.length);
    }

    public ListBufferReader(byte[] data, int start, int end) {
        this.data = data;
        this.start = start;
        this.end = end;
    }

    public ListBufferReader(ListBufferReader reader, int start) {
        this(reader, start, reader.end);
    }

    public ListBufferReader(ListBufferReader reader, int start, int end) {
        this.data = reader.data;
        this.start = start;
        this.end =  end;
    }

    @Override
    public byte read() {
        Byte res = peek();
        start++;
        return res;
    }

    @Override
    public IBufferReader readN(int count) {
        if(count > size()) {
            throw new IndexOutOfBoundsException();
        }
        ListBufferReader reader = new ListBufferReader(data, start, start + count);
        start += count;
        return reader;
    }

    @Override
    public byte peek() {
        return peek(0);
    }

    @Override
    public byte peek(int index) {
        if(size() <= index) {
            throw new IndexOutOfBoundsException();
        }
        return data[start + index];
    }

    @Override
    public int size() {
        return end - start;
    }

    @Override
    public byte[] getArrayCopy() {
        int size = size();
        byte[] copy = new byte[size];
        System.arraycopy(data, start, copy, 0, size);
        return copy;
    }
}
