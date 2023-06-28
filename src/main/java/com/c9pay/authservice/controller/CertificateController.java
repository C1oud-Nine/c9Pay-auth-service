package com.c9pay.authservice.controller;

import com.c9pay.authservice.dto.CertificateForm;
import com.c9pay.authservice.dto.CertificateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/certificate")
public class CertificateController {
    @PostMapping
    public ResponseEntity<CertificateResponse> createCertificate(@RequestBody CertificateForm certificateForm)
            throws NoSuchAlgorithmException {
        // todo 인증서 생성 로직 작성

        return ResponseEntity.ok(new CertificateResponse("dummy-certificate"));
    }
}
