package com.pelizzaris.springsecurity.controller.dto;

public record LoginRequest(
        String email,
        String password) {
}
