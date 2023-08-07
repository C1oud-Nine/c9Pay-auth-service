package com.c9pay.authservice.certificate;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CertificateProvider {
    private final KeyAlgorithm keyAlgorithm;
    private KeyPair keyPair;
    private JcaX509CertificateConverter x509CertificateConverter;
    private String securityProvider;

    @PostConstruct
    public void createKeyPair() {
        Security.addProvider(new BouncyCastleProvider());
        keyPair = keyAlgorithm.createKeyPair();
        securityProvider = BouncyCastleProvider.PROVIDER_NAME;
        x509CertificateConverter = new JcaX509CertificateConverter().setProvider(securityProvider);
    }

    /**
     * Object를 받아서 Certificate를 반환
     */
    public Optional<String> getCertificate(String issuerStr, String subjectStr, String pubkey) {
        try {
            byte[] decode = Base64.getDecoder().decode(pubkey);
            X509EncodedKeySpec pkeySpec = new X509EncodedKeySpec(decode);
            PublicKey subjectKey = keyAlgorithm.getKeyFactory().generatePublic(pkeySpec);

            JcaX509ExtensionUtils x509ExtensionUtils = new JcaX509ExtensionUtils();
            String signatureAlgorithm = "sha256WithRSA";

            X500Name issuer = new X500NameBuilder()
                    .addRDN(BCStyle.CN, issuerStr)
                    .build();

            X500Name subject = new X500NameBuilder()
                    .addRDN(BCStyle.CN, subjectStr)
                    .build();

            BigInteger serialNumber = new BigInteger(128, new SecureRandom());
            ZonedDateTime now = ZonedDateTime.now();
            Date notBefore = Date.from(now.toInstant());
            Date notAfter = Date.from(now.plus(30, ChronoUnit.MONTHS).toInstant());

            X509CertificateHolder rootCertHolder = new JcaX509v3CertificateBuilder(issuer, serialNumber, notBefore, notAfter, subject, subjectKey)
                    .build(new JcaContentSignerBuilder(signatureAlgorithm).build(keyPair.getPrivate()));

            X509Certificate rootCert = x509CertificateConverter.getCertificate(rootCertHolder);
            byte[] encoded = rootCert.getEncoded();
            return Optional.of(new String(Base64.getEncoder().encode(encoded)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Certificate decode(String certificate) {
        try {

            byte[] decode = Base64.getDecoder().decode(certificate.getBytes());
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            return cf.generateCertificate(new ByteArrayInputStream(decode));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }
}
