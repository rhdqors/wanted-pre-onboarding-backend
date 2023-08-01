package com.example.wanted.domain.post.service;

import com.example.wanted.domain.post.dto.request.CreatePostRequestDto;
import com.example.wanted.domain.post.dto.response.AllPostResponseDto;
import com.example.wanted.domain.post.dto.response.DetailPostResponseDto;
import com.example.wanted.domain.post.entity.Post;
import com.example.wanted.domain.post.repository.PostRepository;
import com.example.wanted.domain.user.entity.User;
import com.example.wanted.global.exception.GlobalErrorCode;
import com.example.wanted.global.exception.GlobalException;
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

    // 게시글 상세 조회
    @Transactional
    public DetailPostResponseDto getDetailPost(Long id) {
        Post post = findPost(id);
        return new DetailPostResponseDto(post);
    }

    // 게시글 존재 확인
    public Post findPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.POST_NOT_FOUND));
    }
}
