package com.c9pay.authservice.controller;

import com.c9pay.authservice.dto.PublicKeyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeyController {
    @GetMapping("/public-key")
    public ResponseEntity<PublicKeyResponse> getPublicKey() {
        // todo 저장된 public-key를 가져오는 로직

        return ResponseEntity.ok(new PublicKeyResponse("dummy-key"));
    }
}
