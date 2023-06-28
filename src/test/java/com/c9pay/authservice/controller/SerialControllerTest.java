package com.c9pay.authservice.controller;

import com.c9pay.authservice.entity.SerialNumber;
import com.c9pay.authservice.repository.SerialNumberRepository;
import com.c9pay.authservice.service.SerialNumberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SerialControllerTest {
    @Mock
    SerialNumberRepository serialNumberRepository;
    @InjectMocks
    SerialNumberService service;

    UUID uuid;
    SerialNumber serialNumber;

    @BeforeEach
    public void setUp() {
        uuid = UUID.randomUUID();
        serialNumber = new SerialNumber(uuid);
        given(serialNumberRepository.save(any())).willReturn(serialNumber);
    }

    @Test
    public void 개체식별번호_생성() {
        // given
        
        // when 
        UUID created = service.createSerialNumber();

        // then
        assertEquals(uuid, created);
    }
}