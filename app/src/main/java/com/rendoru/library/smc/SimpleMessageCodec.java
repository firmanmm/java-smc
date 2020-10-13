package com.rendoru.library.smc;

import com.rendoru.library.smc.encoder.ValueEncoder;
import com.rendoru.library.smc.encoder.buffer.ListBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferWriter;

public class SimpleMessageCodec {

    private ValueEncoder valueEncoder;

    public byte[] encode(Object value) {
        ListBufferWriter writer = new ListBufferWriter();
        valueEncoder.encode(value, writer);
        return writer.getArrayCopy();
    }

    public Object decode(byte[] data) {
        ListBufferReader reader = new ListBufferReader(data);
        return valueEncoder.decode(reader);
    }
}
