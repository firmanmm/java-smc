package com.rendoru.library.smc.encoder;

import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.IBufferWriter;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValueEncoder implements IEncoder {

    private interface ICaster {
        Object cast(Object object);
    }

    public final static byte BYTE_VALUE_ENCODER = 1;
    public final static byte INT_VALUE_ENCODER = 2;
    public final static byte UINT_VALUE_ENCODER = 3;
    public final static byte FLOAT_VALUE_ENCODER = 4;
    public final static byte BYTE_ARRAY_VALUE_ENCODER = 6;
    public final static byte STRING_VALUE_ENCODER = 7;
    public final static byte LIST_VALUE_ENCODER = 8;
    public final static byte MAP_VALUE_ENCODER = 9;
    public final static byte BOOL_VALUE_ENCODER = 10;
    public final static byte CLASS_VALUE_ENCODER = 11;
    public final static byte GENERAL_VALUE_ENCODER = (byte) 0b1111;

    private Map<Byte, IEncoder> encoderDispatcher;
    private static Map<Class, Byte> classDispatcher;
    private static Map<Class, ICaster> casterDispatcher;

    public ValueEncoder() {
        encoderDispatcher = new HashMap<>();
        if (classDispatcher == null) {
            classDispatcher = new HashMap<>();
            classDispatcher.put(Long.class, INT_VALUE_ENCODER);
            classDispatcher.put(String.class, STRING_VALUE_ENCODER);
            classDispatcher.put(Double.class, FLOAT_VALUE_ENCODER);
            classDispatcher.put(byte[].class, BYTE_ARRAY_VALUE_ENCODER);
            classDispatcher.put(List.class, LIST_VALUE_ENCODER);
            classDispatcher.put(Map.class, MAP_VALUE_ENCODER);
            classDispatcher.put(Boolean.class, BOOL_VALUE_ENCODER);

            casterDispatcher = new HashMap<>();
            casterDispatcher.put(Byte.class, (Object obj) -> (long) (byte) obj);
            casterDispatcher.put(Short.class, (Object obj) -> (long) (short) obj);
            casterDispatcher.put(Integer.class, (Object obj) -> (long) (int) obj);

            casterDispatcher.put(Float.class, (Object obj) -> (double) (float) obj);
        }
    }

    @Override
    public void encode(Object object, IBufferWriter writer) {
        Byte encoderType;
        if(object instanceof List) {
            encoderType = LIST_VALUE_ENCODER;
        } else if(object instanceof Map) {
            encoderType = MAP_VALUE_ENCODER;
        } else {
            Class objClass = object.getClass();
            ICaster caster = casterDispatcher.get(objClass);
            if (caster != null) {
                object = caster.cast(object);
            }
            Class classObject = object.getClass();
            encoderType = classDispatcher.get(classObject);
            if (encoderType == null) {
                if (Modifier.isFinal(classObject.getModifiers())) {
                    encoderType = GENERAL_VALUE_ENCODER;
                } else {
                    throw new EncoderNotFoundException(classObject);
                }
            }
        }

        doEncode(encoderType, object, writer);
    }

    private void doEncode(Byte type, Object object, IBufferWriter bufferWriter) {
        IEncoder encoder = encoderDispatcher.get(type);
        if (encoder == null) {
            throw new EncoderNotFoundException(object.getClass());
        }
        bufferWriter.write(type);
        encoder.encode(object, bufferWriter);
    }

    @Override
    public Object decode(IBufferReader reader) {
        Byte type = reader.read();
        IEncoder encoder = encoderDispatcher.get(type);
        if (encoder == null) {
            throw new EncoderNotFoundException(type);
        }
        return encoder.decode(reader);
    }

    public void setEncoder(byte encoderType, IEncoder encoder) {
        encoderDispatcher.put(encoderType, encoder);
    }

    public static long unpack(byte data) {
        return data & 0xFF;
    }

    public static byte pack(long data) {
        return (byte) data;
    }

    public static byte[] toPrimitive(Byte[] data) {
        byte[] converted = new byte[data.length];
        int i = 0;
        for (Byte raw : data) {
            converted[i++] = raw;
        }
        return converted;
    }
}
