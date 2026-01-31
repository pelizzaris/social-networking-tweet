package com.pelizzaris.springsecurity.controller.dto;

import java.time.Instant;

public record FeedItemDto(long twwetId, String content, String authorName, Instant createdAt) {
}
