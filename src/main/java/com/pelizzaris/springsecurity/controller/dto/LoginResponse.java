package com.pelizzaris.springsecurity.controller.dto;

public record LoginResponse(
        String accessToken,
        Long expiresIn) {
}
