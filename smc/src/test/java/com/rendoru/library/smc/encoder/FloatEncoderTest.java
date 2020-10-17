package com.rendoru.library.smc.encoder;

import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferWriter;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class FloatEncoderTest {

    @Test
    public void general() {
        double testData = 123456789.123456789;
        IEncoder encoder = FloatEncoder.getInstance();
        ListBufferWriter listBufferWriter = new ListBufferWriter();
        encoder.encode(testData, listBufferWriter);
        byte[] result = listBufferWriter.getArrayCopy();
        IBufferReader reader = new ListBufferReader(result);
        double decoded = (double) encoder.decode(reader);
        assertEquals(testData, decoded, 0.000001);
    }

    @Test
    public void compabilityB64() {
        String input = "hBXNWweIAAAAAKBuzQ8=";
        IEncoder encoder = FloatEncoder.getInstance();
        ListBufferWriter listBufferWriter = new ListBufferWriter();
        encoder.encode((double)123456789.123456789, listBufferWriter);
        byte[] result = listBufferWriter.getArrayCopy();
        String textResult = java.util.Base64.getEncoder().encodeToString(result);
        Assert.assertEquals(input, textResult);
    }

}