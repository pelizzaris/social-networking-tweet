package com.pelizzaris.springsecurity.controller.dto;

import java.util.List;

public record ListAllTweetDto(List<FeedItemDto> tweets, int page, int pageSize, int totalPages, Long totalElements) {
}
