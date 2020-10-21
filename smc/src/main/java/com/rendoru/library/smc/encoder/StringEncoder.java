package com.rendoru.library.smc.encoder;

import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.IBufferWriter;

public class StringEncoder implements IEncoder {

    private IntegerEncoder integerEncoder;
    private static StringEncoder cache;

    private StringEncoder() {
        integerEncoder = IntegerEncoder.getInstance();
    }

    @Override
    public void encode(Object object, IBufferWriter writer) {
        String data = (String)object;
        byte[] byteData = data.getBytes();
        integerEncoder.encode((long)byteData.length, writer);
        writer.write(byteData);
    }

    @Override
    public Object decode(IBufferReader reader) {
        long length = (long)integerEncoder.decode(reader);
        IBufferReader data = reader.readN((int)length);
        return new String(data.getArrayCopy());
    }

    public static StringEncoder getInstance() {
        if(cache == null){
            cache = new StringEncoder();
        }
        return cache;
    }
}
