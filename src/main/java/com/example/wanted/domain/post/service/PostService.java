package com.example.wanted.domain.post.service;

import com.example.wanted.domain.post.dto.request.CreatePostRequestDto;
import com.example.wanted.domain.post.dto.response.AllPostResponseDto;
import com.example.wanted.domain.post.entity.Post;
import com.example.wanted.domain.post.repository.PostRepository;
import com.example.wanted.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 게시글 작성
    public void createPost(CreatePostRequestDto createPostRequestDto, User user) {
        postRepository.saveAndFlush(new Post(createPostRequestDto, user));
    }

    // 게시글 전체 조회
    @Transactional
    public Page<AllPostResponseDto> getAllPosts(Pageable pageable) {
        return postRepository.findAllByOrderByIdAsc(pageable)
                .map(post -> new AllPostResponseDto(post.getId(), post.getTitle()));
    }
}
