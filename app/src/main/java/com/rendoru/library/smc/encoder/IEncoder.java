package com.rendoru.library.smc.encoder;

import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.IBufferWriter;

public interface IEncoder {
    void encode(Object object, IBufferWriter writer);
    Object decode(IBufferReader reader);
}
