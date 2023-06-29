package com.c9pay.authservice.web.controller;

import com.c9pay.authservice.web.dto.SerialNumberResponse;
import com.c9pay.authservice.web.service.SerialNumberService;
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

        boolean isValid = serialNumberService.verifySerialNumber(serialNumber);

        if (isValid) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<SerialNumberResponse> createSerialNumber() {

        UUID serialNumber = serialNumberService.createSerialNumber();

        return ResponseEntity.ok(new SerialNumberResponse(serialNumber));
    }
}
