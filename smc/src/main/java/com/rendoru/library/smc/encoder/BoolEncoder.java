package com.rendoru.library.smc.encoder;

import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.IBufferWriter;

public class BoolEncoder implements IEncoder{

    private static BoolEncoder cache;
    private  BoolEncoder() {}

    @Override
    public void encode(Object object, IBufferWriter writer) {
        boolean data = (boolean)object;
        if(data){
            writer.write((byte)1);
        }else  {
            writer.write((byte)0);
        }
    }

    @Override
    public Object decode(IBufferReader reader) {
        byte data = reader.read();
        return  data > 0;
    }

    public static BoolEncoder getInstance() {
        if(cache == null) {
            cache = new BoolEncoder();
        }
        return cache;
    }
}
