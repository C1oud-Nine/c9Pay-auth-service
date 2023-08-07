package com.c9pay.authservice.web.controller;

import com.c9pay.authservice.certificate.CertificateProvider;
import com.c9pay.authservice.web.dto.CertificateForm;
import com.c9pay.authservice.web.dto.CertificateResponse;
import com.c9pay.authservice.web.dto.ServiceInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/certificate")
public class CertificateController {

    private final CertificateProvider certificateProvider;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<CertificateResponse> createCertificate(@RequestBody CertificateForm certificateForm) throws JsonProcessingException {

        ServiceInfo serviceInfo = certificateForm.getServiceInfo();
        String pubkey = certificateForm.getPublicKey();
        String subject = objectMapper.writeValueAsString(serviceInfo);
        Optional<String> nullable = certificateProvider.getCertificate("auth", subject, pubkey);

        // todo 인증서 생성 불가 에러코드 정의
        return nullable
                .map(certificate ->
                        ResponseEntity.ok(new CertificateResponse(certificate)))
                .orElseGet(() ->
                        ResponseEntity.badRequest().build());
    }
}
