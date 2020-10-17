package com.rendoru.library.smc.encoder;

import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferWriter;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoolEncoderTest {

    @Test
    public void general() {
        IEncoder encoder = BoolEncoder.getInstance();
        ListBufferWriter listBufferWriter = new ListBufferWriter();
        encoder.encode(true, listBufferWriter);
        byte[] result = listBufferWriter.getArrayCopy();
        IBufferReader reader = new ListBufferReader(result);
        boolean decoded = (boolean) encoder.decode(reader);
        Assert.assertEquals(true, decoded);
    }

    @Test
    public void encodeCompability() {
        String data = "AQ==";
        IEncoder encoder = BoolEncoder.getInstance();
        ListBufferWriter listBufferWriter = new ListBufferWriter();
        encoder.encode(true, listBufferWriter);
        byte[] result = listBufferWriter.getArrayCopy();
        String textResult = java.util.Base64.getEncoder().encodeToString(result);
        Assert.assertEquals(data, textResult);
    }
}