package com.rendoru.library.smc.encoder;

import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.IBufferWriter;

import java.util.HashMap;
import java.util.Map;

public class MapEncoder implements  IEncoder{

    private IntegerEncoder integerEncoder;
    private ValueEncoder valueEncoder;

    public MapEncoder(ValueEncoder valueEncoder) {
        this.valueEncoder = valueEncoder;
        integerEncoder = IntegerEncoder.getInstance();
    }

    @Override
    public void encode(Object object, IBufferWriter writer) {
        Map data = (Map)object;
        integerEncoder.encode((long)data.size(), writer);
        for(Object entryObj: data.entrySet()) {
            Map.Entry entry = (Map.Entry) entryObj;
            valueEncoder.encode(entry.getKey(), writer);
            valueEncoder.encode(entry.getValue(), writer);
        }
    }

    @Override
    public Object decode(IBufferReader reader) {
        long length = (long)integerEncoder.decode(reader);
        HashMap<Object, Object> data = new HashMap<>();
        for(int i=0;i<length; i++) {
            Object key = valueEncoder.decode(reader);
            Object value = valueEncoder.decode(reader);
            data.put(key, value);
        }
        return data;
    }
}
