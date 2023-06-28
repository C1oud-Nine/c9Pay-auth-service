package com.c9pay.authservice.dto;

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
