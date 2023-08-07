package com.c9pay.authservice.certificate;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.Signature;

public interface KeyAlgorithm {
    public KeyPair createKeyPair();
    public KeyFactory getKeyFactory();
    public Signature getSignature();
    public Cipher getCipher();
    public int getMaxEncodingSize();
}
