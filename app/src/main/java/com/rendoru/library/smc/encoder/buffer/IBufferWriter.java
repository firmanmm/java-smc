package com.rendoru.library.smc.encoder.buffer;

import java.util.List;

public interface IBufferWriter {
    void write(byte data);
    void write(byte[] data);
    void write(byte[] data, int start, int end);
    byte[] getArrayCopy();
}
