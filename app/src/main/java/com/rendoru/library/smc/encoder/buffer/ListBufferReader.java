package com.rendoru.library.smc.encoder.buffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListBufferReader implements IBufferReader {

    private int start, end;
    private List<Byte> data;

    public ListBufferReader(Byte[] data) {
        this(Arrays.asList(data));
    }

    public ListBufferReader(List<Byte> data) {
        this(data, 0, data.size());
    }

    public ListBufferReader(List<Byte> data, int start) {
        this(data, start, data.size());
    }

    public ListBufferReader(List<Byte> data, int start, int end) {
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
        return data.get(start + index);
    }

    @Override
    public int size() {
        return end - start;
    }
}
