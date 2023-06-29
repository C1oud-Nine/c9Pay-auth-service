package com.c9pay.authservice.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Provider;
import java.security.Security;
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
        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            System.out.println(String.format("======%s======", provider.getName()));
            for (Provider.Service service : provider.getServices()) {
                System.out.println(String.format("%s: %s", service.getType(), service.getAlgorithm()));
            }
            System.out.println();
        }
    }

    @Test
    void 암복호화_테스트() throws JsonProcessingException {
        // given
        Hello hello = new Hello("Jaedoo", 25);
        String json = objectMapper.writeValueAsString(hello);

        // when
        Certificate encoded = certificateProvider.getCertificate(hello).get();
        Optional<Hello> decrypt = certificateProvider.decrypt(encoded, Hello.class);

        // then
        assertTrue(decrypt.isPresent());
        assertEquals(hello.name, decrypt.get().name);
        assertEquals(hello.age, decrypt.get().age);
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