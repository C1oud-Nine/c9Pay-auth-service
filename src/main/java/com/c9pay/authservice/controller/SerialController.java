package com.c9pay.authservice.controller;

import com.c9pay.authservice.dto.SerialNumberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/serial-number")
public class SerialController {
    @GetMapping
    public ResponseEntity<?> verifySerialNumber(@RequestParam UUID serialNumber) {
        // todo 식별번호 검증 로직 작성

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<SerialNumberResponse> createSerialNumber() {
        // todo 식별번호 저장 로직 작성

        return ResponseEntity.ok(new SerialNumberResponse(UUID.randomUUID()));
    }
}
