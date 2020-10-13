package com.rendoru.library.smc.encoder;

import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferWriter;

import org.junit.Assert;
import org.junit.Test;

public class StringEncoderTest {

    @Test
    public void general() {
        IEncoder encoder = StringEncoder.getInstance();
        ListBufferWriter listBufferWriter = new ListBufferWriter();
        encoder.encode("This is not a string!\n Right?\n\r", listBufferWriter);
        byte[] result = listBufferWriter.getArrayCopy();
        IBufferReader reader = new ListBufferReader(result);
        String decoded = (String) encoder.decode(reader);
        Assert.assertEquals("This is not a string!\n Right?\n\r", decoded);
    }

    @Test
    public void compabilityB64() {
        String input = "gR9UaGlzIGlzIG5vdCBhIHN0cmluZyEKIFJpZ2h0PwoN";
        IEncoder encoder = StringEncoder.getInstance();
        ListBufferWriter listBufferWriter = new ListBufferWriter();
        encoder.encode("This is not a string!\n Right?\n\r", listBufferWriter);
        byte[] result = listBufferWriter.getArrayCopy();
        String textResult = java.util.Base64.getEncoder().encodeToString(result);
        Assert.assertEquals(input, textResult);
    }
}