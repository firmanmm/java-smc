package com.rendoru.library.smc.encoder;

import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferWriter;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ListEncoderTest {

    @Test
    public void general() {
        ArrayList data = new ArrayList();
        data.add("This is a data");
        data.add(123456789L);
        data.add(123456.1231545D);

        ValueEncoder valueEncoder = new ValueEncoder();
        valueEncoder.setEncoder(ValueEncoder.INT_VALUE_ENCODER, IntegerEncoder.getInstance());
        valueEncoder.setEncoder(ValueEncoder.STRING_VALUE_ENCODER, StringEncoder.getInstance());
        valueEncoder.setEncoder(ValueEncoder.FLOAT_VALUE_ENCODER, FloatEncoder.getInstance());
        IEncoder encoder = new ListEncoder(valueEncoder);
        valueEncoder.setEncoder(ValueEncoder.LIST_VALUE_ENCODER, encoder);
        ListBufferWriter listBufferWriter = new ListBufferWriter();
        encoder.encode(data, listBufferWriter);
        byte[] result = listBufferWriter.getArrayCopy();
        IBufferReader reader = new ListBufferReader(result);
        List decoded = (List) encoder.decode(reader);
        Assert.assertEquals(data, decoded);
    }

    @Test
    public void compabilityB64() {
        String comparable = "gQMHgQ5UaGlzIGlzIGEgZGF0YQKEFc1bBwSDQOIBiAAAAPDShsMP";
        ArrayList data = new ArrayList();
        data.add("This is a data");
        data.add(123456789L);
        data.add(123456.1231545D);
        ValueEncoder valueEncoder = new ValueEncoder();
        valueEncoder.setEncoder(ValueEncoder.INT_VALUE_ENCODER, IntegerEncoder.getInstance());
        valueEncoder.setEncoder(ValueEncoder.STRING_VALUE_ENCODER, StringEncoder.getInstance());
        valueEncoder.setEncoder(ValueEncoder.FLOAT_VALUE_ENCODER, FloatEncoder.getInstance());
        IEncoder encoder = new ListEncoder(valueEncoder);
        valueEncoder.setEncoder(ValueEncoder.LIST_VALUE_ENCODER, encoder);
        ListBufferWriter listBufferWriter = new ListBufferWriter();
        encoder.encode(data, listBufferWriter);
        byte[] result = listBufferWriter.getArrayCopy();
        String textResult = java.util.Base64.getEncoder().encodeToString(result);
        Assert.assertEquals(comparable, textResult);
    }
}