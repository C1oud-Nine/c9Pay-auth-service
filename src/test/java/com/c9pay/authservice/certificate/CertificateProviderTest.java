package com.c9pay.authservice.certificate;

import com.c9pay.authservice.web.dto.CertificateForm;
import com.c9pay.authservice.web.dto.ServiceInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CertificateProviderTest {
    CertificateProvider certificateProvider;
    ObjectMapper objectMapper;
    Rsa rsa;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        rsa = new Rsa();
        certificateProvider = new CertificateProvider(rsa);
        certificateProvider.createKeyPair();
    }

    @Test
    public void 알고리즘목록출력() {
        Security.addProvider(new BouncyCastleProvider());
        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            if (true) {
                System.out.println(String.format("======%s======", provider.getName()));
                for (Provider.Service service : provider.getServices()) {
                    System.out.println(String.format("%s: %s", service.getType(), service.getAlgorithm()));
                }
            }
            System.out.println();
        }
    }

    @Test
    void 암복호화_테스트() throws JsonProcessingException {
        // given
        Hello hello = new Hello("Jaedoo", 25);
        String json = objectMapper.writeValueAsString(hello);
        KeyPair keyPair = rsa.createKeyPair();
        String pubkey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

        // when
        String encoded = certificateProvider.getCertificate("auth", json, pubkey).get();
        Certificate decode = certificateProvider.decode(encoded);
        System.out.println(decode);

        // then
        assertEquals(keyPair.getPublic().toString(), decode.getPublicKey().toString());
        assertDoesNotThrow(()->decode.verify(certificateProvider.getPublicKey()));
    }

    @Test
    @DisplayName("CertificateForm 암복호화")
    void 암복호화_테스트2() throws JsonProcessingException {
        // given
        ServiceInfo expectedServiceInfo = new ServiceInfo("user-service", "/api/testes/test/est/es/dummy");
        CertificateForm expected = new CertificateForm("abcd".repeat(128), expectedServiceInfo);
        PublicKey publicKey = certificateProvider.getPublicKey();
        String base64Encoded = new String(Base64.getEncoder().encode(publicKey.getEncoded()),
                StandardCharsets.UTF_8);

    }

    static class Hello {
        public String name;
        public int age;

        public Hello(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public Hello() {}
    }
}