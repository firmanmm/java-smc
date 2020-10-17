package com.rendoru.library.smc.encoder;
import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferWriter;

import org.junit.Assert;
import org.junit.Test;

public class IntegerEncoderTest {

    @Test
    public void general() {
        IEncoder encoder = IntegerEncoder.getInstance();
        ListBufferWriter listBufferWriter = new ListBufferWriter();
        encoder.encode(123456789L, listBufferWriter);
        byte[] result = listBufferWriter.getArrayCopy();
        IBufferReader reader = new ListBufferReader(result);
        long decoded = (long) encoder.decode(reader);
        Assert.assertEquals(123456789L, decoded);
    }

    @Test
    public void encodeCompability() {
        String data = "hBXNWwc=";
        IEncoder encoder = IntegerEncoder.getInstance();
        ListBufferWriter listBufferWriter = new ListBufferWriter();
        encoder.encode((long)123456789, listBufferWriter);
        byte[] result = listBufferWriter.getArrayCopy();
        String textResult = java.util.Base64.getEncoder().encodeToString(result);
        Assert.assertEquals(data, textResult);
    }
}