package com.c9pay.authservice.repository;

import com.c9pay.authservice.entity.SerialNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SerialNumberRepository extends JpaRepository<SerialNumber, UUID> {
}
