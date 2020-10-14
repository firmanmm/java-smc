package com.rendoru.library.smc.encoder;

import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.IBufferWriter;

public class ByteArrayEncoder implements IEncoder {

    private IntegerEncoder integerEncoder;
    private static ByteArrayEncoder cache;

    private ByteArrayEncoder() {
        integerEncoder = IntegerEncoder.getInstance();
    }

    @Override
    public void encode(Object object, IBufferWriter writer) {
        byte[] data = (byte[]) object;
        integerEncoder.encode((long)data.length, writer);
        writer.write(data);
    }

    @Override
    public Object decode(IBufferReader reader) {
        long length = (long)integerEncoder.decode(reader);
        IBufferReader data = reader.readN((int)length);
        return data.getArrayCopy();
    }

    public static ByteArrayEncoder getInstance() {
        if(cache == null){
            cache = new ByteArrayEncoder();
        }
        return cache;
    }
}
