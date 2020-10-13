package com.rendoru.library.smc;

import com.rendoru.library.smc.encoder.ValueEncoder;
import com.rendoru.library.smc.encoder.buffer.ListBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferWriter;

public class SimpleMessageCodec {

    private ValueEncoder valueEncoder;

    public byte[] encode(Object value) {
        ListBufferWriter writer = new ListBufferWriter();
        valueEncoder.encode(value, writer);
        Byte[] data = writer.getContent();
        return ValueEncoder.toPrimitive(data);
    }

    public Object decode(byte[] data) {
        Byte[] converted = new Byte[data.length];
        int i = 0;
        for (byte raw : data) {
            converted[i++] = raw;
        }
        return decode(converted);
    }

    public Object decode(Byte[] data) {
        ListBufferReader reader = new ListBufferReader(data);
        return valueEncoder.decode(reader);
    }
}
