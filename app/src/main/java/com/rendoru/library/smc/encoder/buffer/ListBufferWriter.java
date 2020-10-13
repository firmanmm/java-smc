package com.rendoru.library.smc.encoder.buffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListBufferWriter implements IBufferWriter {

    public List<Byte> buffer;

    public ListBufferWriter() {
        buffer = new ArrayList<>();
    }

    @Override
    public void write(byte data) {
        buffer.add(data);
    }

    @Override
    public void write(List<Byte> datas) {
        buffer.addAll(datas);
    }

    @Override
    public void write(Byte[] data, int start, int end) {
        buffer.addAll(Arrays.asList(data).subList(start, end));
    }

    @Override
    public Byte[] getContent() {
        List<Byte> newBuffer = buffer;
        buffer = new ArrayList<>();
        return newBuffer.toArray(new Byte[0]);
    }
}
