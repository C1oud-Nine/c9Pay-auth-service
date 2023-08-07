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
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CertificateProviderTest {
    CertificateProvider certificateProvider;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        Rsa rsa = new Rsa();
        certificateProvider = new CertificateProvider(rsa, objectMapper);
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
        System.out.println(json.length());

        // when
        Certificate encoded = certificateProvider.getCertificate(hello).get();
        System.out.println(encoded.getCertificate().length());
        Optional<Hello> decrypt = certificateProvider.decrypt(encoded, Hello.class);

        // then
        assertTrue(decrypt.isPresent());
        assertEquals(hello.name, decrypt.get().name);
        assertEquals(hello.age, decrypt.get().age);
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

        // when
        Certificate encoded = certificateProvider.getCertificate(expected).get();
        Optional<CertificateForm> decrypt = certificateProvider.decrypt(encoded, CertificateForm.class);

        // then
        assertTrue(decrypt.isPresent());
        assertEquals(expected.toString(), decrypt.get().toString());

        System.out.println("Certification: " + encoded.getCertificate());
        System.out.println("Signature: " + encoded.getSign());
        System.out.println("PublicKey: " + base64Encoded);
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