package com.rendoru.library.smc.encoder;

import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferWriter;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ByteArrayEncoderTest {

    @Test
    public void general() {
        byte[] data = new byte[256];
        for(int i=0;i<256;i++){
            data[i] = (byte)i;
        }
        IEncoder encoder = ByteArrayEncoder.getInstance();
        ListBufferWriter listBufferWriter = new ListBufferWriter();
        encoder.encode(data, listBufferWriter);
        byte[] result = listBufferWriter.getArrayCopy();
        IBufferReader reader = new ListBufferReader(result);
        byte[] decoded = (byte[]) encoder.decode(reader);
        assertArrayEquals(data, decoded);
    }

    @Test
    public void compabilityB64() {
        String comparable = "ggABAAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8gISIjJCUmJygpKissLS4vMDEyMzQ1Njc4OTo7PD0+P0BBQkNERUZHSElKS0xNTk9QUVJTVFVWV1hZWltcXV5fYGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl6e3x9fn+AgYKDhIWGh4iJiouMjY6PkJGSk5SVlpeYmZqbnJ2en6ChoqOkpaanqKmqq6ytrq+wsbKztLW2t7i5uru8vb6/wMHCw8TFxsfIycrLzM3Oz9DR0tPU1dbX2Nna29zd3t/g4eLj5OXm5+jp6uvs7e7v8PHy8/T19vf4+fr7/P3+/w==";
        byte[] data = new byte[256];
        for(int i=0;i<256;i++){
            data[i] = (byte)i;
        }
        IEncoder encoder = ByteArrayEncoder.getInstance();
        ListBufferWriter listBufferWriter = new ListBufferWriter();
        encoder.encode(data, listBufferWriter);
        byte[] result = listBufferWriter.getArrayCopy();
        String textResult = java.util.Base64.getEncoder().encodeToString(result);
        Assert.assertEquals(comparable, textResult);
    }
}