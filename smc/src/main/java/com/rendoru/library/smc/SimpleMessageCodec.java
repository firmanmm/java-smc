package com.rendoru.library.smc;

import com.rendoru.library.smc.encoder.BoolEncoder;
import com.rendoru.library.smc.encoder.ByteArrayEncoder;
import com.rendoru.library.smc.encoder.FloatEncoder;
import com.rendoru.library.smc.encoder.IntegerEncoder;
import com.rendoru.library.smc.encoder.ListEncoder;
import com.rendoru.library.smc.encoder.MapEncoder;
import com.rendoru.library.smc.encoder.StringEncoder;
import com.rendoru.library.smc.encoder.ValueEncoder;
import com.rendoru.library.smc.encoder.buffer.IBufferReader;
import com.rendoru.library.smc.encoder.buffer.IBufferWriter;
import com.rendoru.library.smc.encoder.buffer.ListBufferReader;
import com.rendoru.library.smc.encoder.buffer.ListBufferWriter;

import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleMessageCodec {

    private ValueEncoder valueEncoder;
    private static InternalBufferPool pool = new InternalBufferPool();
    private static SimpleMessageCodec defaultCodec = new SimpleMessageCodec();

    //Return a SimpleMessageCodec with default encoder
    public SimpleMessageCodec() {
        valueEncoder = getDefaultEncoder();
    }

    //Allow you to set which encoder to use
    public SimpleMessageCodec(ValueEncoder valueEncoder) {
        this.valueEncoder = valueEncoder;
    }

    //Automatically encode value into array of bytes.
    //The order are determined by the encoder.
    //Can only be decoded using [decode] function.
    public byte[] encode(Object value) {
        IBufferWriter writer = pool.acquire();
        try {
            valueEncoder.encode(value, writer);
            return writer.getArrayCopy();
        } finally {
            pool.release(writer);
        }
    }

    //Automatically decode value that is encoded using [encode] function
    public Object decode(byte[] data) {
        IBufferReader reader = new ListBufferReader(data);
        return valueEncoder.decode(reader);
    }

    private ValueEncoder getDefaultEncoder() {
        ValueEncoder valueEncoder = new ValueEncoder();
        valueEncoder.setEncoder(ValueEncoder.BOOL_VALUE_ENCODER, BoolEncoder.getInstance());
        valueEncoder.setEncoder(ValueEncoder.INT_VALUE_ENCODER, IntegerEncoder.getInstance());
        valueEncoder.setEncoder(ValueEncoder.FLOAT_VALUE_ENCODER, FloatEncoder.getInstance());
        valueEncoder.setEncoder(ValueEncoder.BYTE_ARRAY_VALUE_ENCODER, ByteArrayEncoder.getInstance());
        valueEncoder.setEncoder(ValueEncoder.STRING_VALUE_ENCODER, StringEncoder.getInstance());

        ListEncoder listEncoder = new ListEncoder(valueEncoder);
        valueEncoder.setEncoder(ValueEncoder.LIST_VALUE_ENCODER, listEncoder);
        MapEncoder mapEncoder = new MapEncoder(valueEncoder);
        valueEncoder.setEncoder(ValueEncoder.MAP_VALUE_ENCODER, mapEncoder);
        return valueEncoder;
    }

    //Define how data should be encoded
    public interface IManualEncoder {
        void execute(Object data, IWriter writer);

        interface IWriter {
            void write(Object object);
        }
    }

    //Manually encode a data
    //You are responsible to determine the data order
    //May gain performance increase if all encoded data are natively supported
    //Useful if you want to encode arbitrary data
    public byte[] manualEncode(Object data, IManualEncoder manualEncoder) {
        IBufferWriter writer = pool.acquire();
        try {
            manualEncoder.execute(data, (Object object) -> valueEncoder.encode(object, writer));
            return writer.getArrayCopy();
        } finally {
            pool.release(writer);
        }
    }

    public interface IManualDecoder {
        Object execute(IReader reader);

        interface IReader {
            Object read();
        }
    }

    //Manually decode a data
    //You are responsible to determine the data order
    //Data order is the same as in the [manualEncode]
    //Useful if you want to decode arbitrary data
    public Object manualDecode(byte[] data, IManualDecoder manualReader) {
        IBufferReader bufferReader = new ListBufferReader(data);
        return manualReader.execute(() -> valueEncoder.decode(bufferReader));
    }

    public static byte[] defaultEncode(Object object) {
        return defaultCodec.encode(object);
    }

    public static Object defaultDecode(byte[] data) {
        return defaultCodec.decode(data);
    }

    //Manually encode a data
    //You are responsible to determine the data order
    //May gain performance increase if all encoded data are natively supported
    //Useful if you want to encode arbitrary data
    public static byte[] defaultManualEncode(Object data, IManualEncoder manualEncoder) {
       return defaultCodec.manualEncode(data, manualEncoder);
    }

    //Manually decode a data
    //You are responsible to determine the data order
    //Data order is the same as in the [manualEncode]
    //Useful if you want to decode arbitrary data
    public static Object defaultManualDecode(byte[] data, IManualDecoder manualReader) {
        return defaultCodec.manualDecode(data, manualReader);
    }

    public static SimpleMessageCodec getDefaultInstance() {
        return defaultCodec;
    }
}

class InternalBufferPool {

    private ReentrantLock lock;
    private Stack<IBufferWriter> stack;

    public InternalBufferPool() {
        lock = new ReentrantLock();
        stack = new Stack<>();
    }

    public IBufferWriter acquire() {
        try {
            lock.lock();
            if (stack.size() == 0) {
                return new ListBufferWriter();
            } else {
                return stack.pop();
            }
        } finally {
            lock.unlock();
        }
    }

    public void release(IBufferWriter writer) {
        try {
            lock.lock();
            writer.reset();
            stack.push(writer);
        } finally {
            lock.unlock();
        }
    }

}
