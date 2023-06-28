package com.c9pay.authservice.controller;

import com.c9pay.authservice.dto.SerialNumberResponse;
import com.c9pay.authservice.service.SerialNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/serial-number")
public class SerialController {
    private final SerialNumberService serialNumberService;
    @GetMapping
    public ResponseEntity<?> verifySerialNumber(@RequestParam UUID serialNumber) {
        // todo 식별번호 검증 로직 작성

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<SerialNumberResponse> createSerialNumber() {

        UUID serialNumber = serialNumberService.createSerialNumber();

        return ResponseEntity.ok(new SerialNumberResponse(serialNumber));
    }
}
