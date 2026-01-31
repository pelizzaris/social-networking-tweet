package com.pelizzaris.springsecurity.controller.dto;

public record UserRequestDto(
        String name,
        String email,
        String password) {
}
