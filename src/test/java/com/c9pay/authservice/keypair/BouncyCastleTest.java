package com.c9pay.authservice.keypair;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;

public class BouncyCastleTest {
    @Test
    public void test() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECIES", "BC");
        keyPairGenerator.initialize(384 , new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("ECIES", "BC");
    }
}
