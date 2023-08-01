package com.example.wanted.domain.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatePostRequestDto {

    private String title;
    private String content;
}
