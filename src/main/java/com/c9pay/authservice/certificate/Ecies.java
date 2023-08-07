package com.c9pay.authservice.certificate;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Ecies implements KeyAlgorithm {
    private final int KEY_SIZE = 1024;
    @Override
    public KeyPair createKeyPair() {
        ECGenParameterSpec ecGenSpec = new ECGenParameterSpec("EC");
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            keyGen.initialize(ecGenSpec, new SecureRandom());

            return keyGen.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Signature getSignature() {
        return null;
    }

    @Override
    public Cipher getCipher() {
        return null;
    }

    @Override
    public int getMaxEncodingSize() {
        return 0;
    }
}
