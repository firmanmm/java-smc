package com.rendoru.library.smc.encoder;
import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferWriter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runners.JUnit4;

import java.util.Base64;

public class IntegerEncoderTest {

    @Test
    public void general() {
        IEncoder encoder = new IntegerEncoder();
        ListBufferWriter listBufferWriter = new ListBufferWriter();
        encoder.encode(123456789L, listBufferWriter);
        Byte[] result = listBufferWriter.getContent();
        IBufferReader reader = new ListBufferReader(result);
        long decoded = (long) encoder.decode(reader);
        Assert.assertEquals(123456789L, decoded);
    }

    @Test
    public void encodeCompability() {
        String data = "hBXNWwc=";
        IEncoder encoder = new IntegerEncoder();
        ListBufferWriter listBufferWriter = new ListBufferWriter();
        encoder.encode((long)123456789, listBufferWriter);
        Byte[] result = listBufferWriter.getContent();
        String textResult = java.util.Base64.getEncoder().encodeToString(ValueEncoder.toPrimitive(result));
        Assert.assertEquals(data, textResult);
    }
}