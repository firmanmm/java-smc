package com.rendoru.library.smc.encoder;

import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.IBufferWriter;

import java.util.ArrayList;
import java.util.List;

public class ListEncoder implements IEncoder {

    private ValueEncoder valueEncoder;
    private IntegerEncoder integerEncoder;
    public ListEncoder(ValueEncoder valueEncoder) {
        this.valueEncoder = valueEncoder;
        integerEncoder = IntegerEncoder.getInstance();
    }

    @Override
    public void encode(Object object, IBufferWriter writer) {
        List data = (List)object;
        integerEncoder.encode((long)data.size(), writer);
        for(Object child: data) {
            valueEncoder.encode(child, writer);
        }
    }

    @Override
    public Object decode(IBufferReader reader) {
        long length = (long)integerEncoder.decode(reader);
        ArrayList data = new ArrayList((int)length);
        for(int i=0;i<length;i++){
            data.add(valueEncoder.decode(reader));
        }
        return data;
    }
}
