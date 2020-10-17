package com.rendoru.library.smc.encoder.buffer;

public interface IBufferReader {
    byte read();
    IBufferReader readN(int count);
    byte peek();
    byte peek(int index);
    int size();
    byte[] getArrayCopy();
}
