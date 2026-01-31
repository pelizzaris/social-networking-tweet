package com.pelizzaris.springsecurity.controller.dto;

public record LoginRequestDto(
        String email,
        String password) {
}
