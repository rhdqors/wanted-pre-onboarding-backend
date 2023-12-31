package com.example.wanted.domain.post.controller;

import com.example.wanted.domain.post.dto.request.CreatePostRequestDto;
import com.example.wanted.domain.post.dto.response.AllPostResponseDto;
import com.example.wanted.domain.post.dto.response.CustomPaginatedResponseDto;
import com.example.wanted.domain.post.service.PostService;
import com.example.wanted.domain.user.security.UserDetailsImpl;
import com.example.wanted.global.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 작성
    @PostMapping()
    public ResponseEntity<?> createPost(@RequestBody CreatePostRequestDto createPostRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.createPost(createPostRequestDto, userDetails.getUser());
        return ResponseMessage.SuccessResponse("게시글 작성 완료", "");
    }

    // 게시글 전체 조회
    @GetMapping()
    public ResponseEntity<?> getAllPosts(@RequestParam(required = false) Integer page,
                                         @RequestParam(required = false, defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page != null ? page : 0, size);
        Page<AllPostResponseDto> postsPage = postService.getAllPosts(pageable);
        CustomPaginatedResponseDto<AllPostResponseDto> paginatedResponse =
                new CustomPaginatedResponseDto<>(postsPage.getContent(), postsPage.getTotalPages(), postsPage.getTotalElements());
        return ResponseMessage.SuccessResponse("게시글 전체 조회 완료", paginatedResponse);
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailPost(@PathVariable Long id) {
        return ResponseMessage.SuccessResponse("게시글 상세 조회 완료", postService.getDetailPost(id));
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> editPost(@PathVariable Long id, @RequestBody CreatePostRequestDto createPostRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.editPost(id, createPostRequestDto, userDetails.getUser());
        return ResponseMessage.SuccessResponse("게시글 수정 완료", "");
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(id, userDetails.getUser());
        return ResponseMessage.SuccessResponse("게시글 삭제 완료", "");
    }
}
