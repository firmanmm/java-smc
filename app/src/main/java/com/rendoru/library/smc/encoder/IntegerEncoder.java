package com.rendoru.library.smc.encoder;

import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.IBufferWriter;

public class IntegerEncoder implements IEncoder {

    private final static int INT_ENCODER_MAX_ARRAY_LIMIT = 8;
    private static IntegerEncoder cache;

    @Override
    public void encode(Object object, IBufferWriter writer) {
        Long data = (Long)object;
        Byte[] byteArray = new Byte[INT_ENCODER_MAX_ARRAY_LIMIT];
        int spaceUsed = 0;
        boolean isPositive = data > 0;
        if(isPositive){
            data = -data;
        }
        while(data < 0) {
            byteArray[spaceUsed] = (byte)(-data % 256);
            data /= 256;
            spaceUsed++;
        }
        int length = spaceUsed;
        if(isPositive){
            length |= 128;
        }
        writer.write((byte)length);
        writer.write(byteArray,0, spaceUsed);
    }

    @Override
    public Object decode(IBufferReader reader) {
        long data = 0;
        long multiplier = 1;
        int length = reader.read()&0xFF;
        boolean isPositive = (length & 128) > 0;
        if(isPositive){
            length = length ^ 128;
        }
        IBufferReader rawByte = reader.readN(length);
        int size = rawByte.size();
        for(int i=0;i<size;i++){
            long current = rawByte.read()&0xFF;
            data += current * multiplier;
            multiplier *= 256;
        }
        if (!isPositive) {
            data = -data;
        }
        return data;
    }

    public static IntegerEncoder getInstance() {
        if(cache == null) {
            cache = new IntegerEncoder();
        }
        return cache;
    }

}
