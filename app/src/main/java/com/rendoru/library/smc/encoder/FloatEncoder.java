package com.rendoru.library.smc.encoder;

import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.IBufferWriter;

public class FloatEncoder implements IEncoder {

    private IntegerEncoder integerEncoder;
    private static FloatEncoder cache;

    public FloatEncoder() {
        integerEncoder = IntegerEncoder.getInstance();
    }

    @Override
    public void encode(Object object, IBufferWriter writer) {
        double data = (double)object;
        long headPart = (long)data;
        long fracPart = (long)((data - headPart) * (double)Long.MAX_VALUE);
        integerEncoder.encode(headPart, writer);
        integerEncoder.encode(fracPart, writer);
    }

    @Override
    public Object decode(IBufferReader reader) {
        long head = (long)integerEncoder.decode(reader);
        long frac = (long)integerEncoder.decode(reader);
        return (double)head + ((double)frac / (double)Long.MAX_VALUE);
    }

    public static FloatEncoder getInstance() {
        if(cache == null) {
            cache = new FloatEncoder();
        }
        return cache;
    }
}
