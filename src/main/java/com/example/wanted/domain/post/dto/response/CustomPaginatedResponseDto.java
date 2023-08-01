package com.example.wanted.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CustomPaginatedResponseDto<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
}
