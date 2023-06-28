package com.c9pay.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateForm {
    private String publicKey;
    private ServiceInfo serviceInfo;
}
