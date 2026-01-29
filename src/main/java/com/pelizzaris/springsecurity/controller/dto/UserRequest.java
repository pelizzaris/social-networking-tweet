package com.pelizzaris.springsecurity.controller.dto;

public record UserRequest(
        String name,
        String email,
        String password) {
}
