package com.pelizzaris.springsecurity.controller.dto;

public record LoginResponseDto(
        String accessToken,
        Long expiresIn) {
}
