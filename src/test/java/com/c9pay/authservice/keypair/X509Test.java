package com.c9pay.authservice.keypair;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.IEKeySpec;
import org.bouncycastle.jce.spec.IESParameterSpec;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

public class X509Test {
    String certificate;

    @BeforeEach
    void makeCert() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        String securityProvider = BouncyCastleProvider.PROVIDER_NAME;
        JcaX509ExtensionUtils x509ExtensionUtils = new JcaX509ExtensionUtils();
        JcaX509CertificateConverter x509CertificateConverter = new JcaX509CertificateConverter().setProvider(securityProvider);
        String signatureAlgorithm = "sha256WithRSA";

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", securityProvider);
        keyPairGenerator.initialize(2048, new SecureRandom());

        KeyPair rootKeyPair = keyPairGenerator.generateKeyPair();

        X500Name issuer = new X500NameBuilder()
                .addRDN(BCStyle.CN, "Mambo Org")
                .build();

        BigInteger serialNumber = new BigInteger(128, new SecureRandom());

        ZonedDateTime now = ZonedDateTime.now();
        Date notBefore = Date.from(now.toInstant());
        Date notAfter = Date.from(now.plus(30, ChronoUnit.YEARS).toInstant());

        X509CertificateHolder rootCertHolder = new JcaX509v3CertificateBuilder(issuer, serialNumber, notBefore, null, issuer, rootKeyPair.getPublic())
                .addExtension(Extension.basicConstraints, true, new BasicConstraints(true))
                .addExtension(Extension.subjectKeyIdentifier, false, x509ExtensionUtils.createSubjectKeyIdentifier(rootKeyPair.getPublic()))
                .build(new JcaContentSignerBuilder(signatureAlgorithm).build(rootKeyPair.getPrivate()));

        X509Certificate rootCert = x509CertificateConverter.getCertificate(rootCertHolder);
        byte[] encoded = rootCert.getEncoded();
        certificate = new String(Base64.getEncoder().encode(encoded));
    }

    @Test
    void decodingTest() throws Exception {
        byte[] decode = Base64.getDecoder().decode(certificate.getBytes());
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        Certificate cert = cf.generateCertificate(new ByteArrayInputStream(decode));
        System.out.println(cert);
    }

}
