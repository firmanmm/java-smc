package com.rendoru.library.smc.encoder;

public class EncoderNotFoundException extends RuntimeException {
    public EncoderNotFoundException(Object object) {
        super("Key not found for " + object.getClass().toString());
    }

    public EncoderNotFoundException(Byte object) {
        super("Key not found for " + object);
    }
}