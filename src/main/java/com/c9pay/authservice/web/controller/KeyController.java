package com.c9pay.authservice.web.controller;

import com.c9pay.authservice.certificate.CertificateProvider;
import com.c9pay.authservice.web.dto.PublicKeyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
public class KeyController {

    private final CertificateProvider certificateProvider;

    @GetMapping("/public-key")
    public ResponseEntity<PublicKeyResponse> getPublicKey() {
        PublicKey publicKey = certificateProvider.getPublicKey();

        String base64Encoded = new String(Base64.getEncoder().encode(publicKey.getEncoded()),
                                StandardCharsets.UTF_8);

        return ResponseEntity.ok(new PublicKeyResponse(base64Encoded));
    }
}
