package com.c9pay.authservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceInfo {
    private String serviceName;
    private String RecognitionEndpoint;
}
