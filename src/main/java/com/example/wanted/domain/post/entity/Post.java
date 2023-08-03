package com.example.wanted.domain.post.entity;

import com.example.wanted.domain.post.dto.request.CreatePostRequestDto;
import com.example.wanted.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Post(CreatePostRequestDto createPostRequestDto, User user) {
        this.title = createPostRequestDto.getTitle();
        this.content = createPostRequestDto.getContent();
        this.user = user;
    }

    public void updatePost(CreatePostRequestDto createPostRequestDto) {
        this.title = createPostRequestDto.getTitle();
        this.content = createPostRequestDto.getContent();
    }
}
