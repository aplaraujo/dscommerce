package com.example.dscommerce.dto;

import java.time.Instant;

import com.example.dscommerce.entities.Payment;

public record PaymentDTO(
        Long id,
        Instant moment
) {}
