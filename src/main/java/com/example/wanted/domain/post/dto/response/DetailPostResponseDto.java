package com.example.wanted.domain.post.dto.response;

import com.example.wanted.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DetailPostResponseDto {

    private Long id;
    private String title;
    private String content;

    public DetailPostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
