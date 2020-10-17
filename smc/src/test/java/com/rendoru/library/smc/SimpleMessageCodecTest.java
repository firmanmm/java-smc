package com.rendoru.library.smc;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SimpleMessageCodecTest {

    class DummyClass {
        String name;
        byte[] fingerPrint;
        int age;
        boolean active;
        double weight;
        List<String> relative;
    }

    private DummyClass getDummyData() {
        DummyClass dummyClass = new DummyClass();
        dummyClass.name = "Rendoru";

        dummyClass.fingerPrint = new byte[]{1,2,3,4,5,6,7, -1, -12, -21, -4, 44};
        dummyClass.age = 22;
        dummyClass.active = true;
        dummyClass.weight = 123.444;
        dummyClass.relative = Arrays.asList("Doruru", "Ren", "Doru", "XDoru");
        return dummyClass;
    }

    @Test
    public void  auto() {
        HashMap<Object, Object> original = new HashMap<>();
        original.put(1L, 1123.312D);
        original.put("Not A Number", 13123L);
        original.put(-1L, "11111");
        original.put(-2L, -2L);
        original.put("ww", "www");
        HashMap<String, String> nested = new HashMap<>();
        nested.put("A", "Yes a string");
        nested.put("Java is good", "you know");
        nested.put("You can do all of this", "Just because of java");
        original.put("nested", nested);

        SimpleMessageCodec simpleMessageCodec = new SimpleMessageCodec();
        byte[] encoded = simpleMessageCodec.encode(original);
        Object decoded = simpleMessageCodec.decode(encoded);
        Assert.assertEquals(original, decoded);
    }

    @Test
    public void manual() {
        SimpleMessageCodec simpleMessageCodec = new SimpleMessageCodec();
        DummyClass original = getDummyData();
        byte[] encoded = simpleMessageCodec.manualEncode(original, (data, writer) -> {
            DummyClass dummyData = (DummyClass)data;
            writer.write(dummyData.name);
            writer.write(dummyData.age);
            writer.write(dummyData.fingerPrint);
            writer.write(dummyData.active);
            writer.write(dummyData.weight);
            writer.write(dummyData.relative);
        });

        DummyClass decoded = (DummyClass)simpleMessageCodec.manualDecode(encoded, reader -> {
            DummyClass dummyData = new DummyClass();
            dummyData.name = (String) reader.read();
            dummyData.age = (int)(long)reader.read();
            dummyData.fingerPrint = (byte[]) reader.read();
            dummyData.active = (boolean)reader.read();
            dummyData.weight = (double)reader.read();
            dummyData.relative = (List<String>)reader.read();
            return dummyData;
        });
        Assert.assertEquals(original.age, decoded.age);
        Assert.assertEquals(original.name, decoded.name);
        Assert.assertArrayEquals(original.fingerPrint, decoded.fingerPrint);
        Assert.assertEquals(original.active, decoded.active);
        Assert.assertEquals(original.weight, decoded.weight, 0.001);
        Assert.assertEquals(original.relative, decoded.relative);
    }

}