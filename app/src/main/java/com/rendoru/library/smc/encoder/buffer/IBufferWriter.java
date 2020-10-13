package com.rendoru.library.smc.encoder.buffer;

import java.util.List;

public interface IBufferWriter {
    void write(byte data);
    void write(List<Byte> datas);
    void write(Byte[] data, int start, int end);
    Byte[] getContent();
}
