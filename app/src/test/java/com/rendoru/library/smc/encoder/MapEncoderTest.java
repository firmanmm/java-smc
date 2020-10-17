package com.rendoru.library.smc.encoder;

import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferWriter;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MapEncoderTest {
    @Test
    public void compabilityB64() {
        String comparable = "gQUCgQEEgmMEiAAAAC2yne8nB4EMTm90IEEgTnVtYmVyAoJDMwIBAQeBBTExMTExAgECAgECB4ECd3cHgQN3d3c=";
        HashMap<Object, Object> data = new HashMap<>();
        data.put(1L, 1123.312D);
        data.put("Not A Number", 13123L);
        data.put(-1L, "11111");
        data.put(-2L, -2L);
        data.put("ww", "www");
        ValueEncoder valueEncoder = new ValueEncoder();
        valueEncoder.setEncoder(ValueEncoder.INT_VALUE_ENCODER, IntegerEncoder.getInstance());
        valueEncoder.setEncoder(ValueEncoder.STRING_VALUE_ENCODER, StringEncoder.getInstance());
        valueEncoder.setEncoder(ValueEncoder.FLOAT_VALUE_ENCODER, FloatEncoder.getInstance());
        IEncoder encoder = new MapEncoder(valueEncoder);
        valueEncoder.setEncoder(ValueEncoder.MAP_VALUE_ENCODER, encoder);
        byte[] result = java.util.Base64.getDecoder().decode(comparable);
        IBufferReader listBufferReader = new ListBufferReader(result);
        Map original = (Map)encoder.decode(listBufferReader);
        assertEquals(data, original);
    }
}