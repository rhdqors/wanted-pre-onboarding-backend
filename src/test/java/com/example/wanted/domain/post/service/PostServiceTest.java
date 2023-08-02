package com.example.wanted.domain.post.service;

import com.example.wanted.domain.post.dto.request.CreatePostRequestDto;
import com.example.wanted.domain.post.dto.response.AllPostResponseDto;
import com.example.wanted.domain.post.dto.response.DetailPostResponseDto;
import com.example.wanted.domain.post.entity.Post;
import com.example.wanted.domain.post.repository.PostRepository;
import com.example.wanted.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCase {
        @Test
        void createPost() {
// Given
            String title = "Test Title";
            String content = "Test Content";
            User user = new User();
            CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto(title, content);

            // When
            postService.createPost(createPostRequestDto, user);

            // Then
            ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
            verify(postRepository).saveAndFlush(postCaptor.capture());

            Post savedPost = postCaptor.getValue();
            assertThat(savedPost).isNotNull();
            assertThat(savedPost.getTitle()).isEqualTo(title);
            assertThat(savedPost.getContent()).isEqualTo(content);
            assertThat(savedPost.getUser()).isEqualTo(user);
        }

        @Test
        void getAllPosts() {
            // given
            Pageable pageable = PageRequest.of(0, 5);
            Post post1 = new Post(1L, "Title 1", "Content 1", new User());
            Post post2 = new Post(2L, "Title 2", "Content 2", new User());
            Page<Post> postsPage = new PageImpl<>(List.of(post1, post2));
            given(postRepository.findAllByOrderByIdAsc(pageable)).willReturn(postsPage.map(post -> new AllPostResponseDto(post.getId(), post.getTitle())));

            // when
            Page<AllPostResponseDto> result = postService.getAllPosts(pageable);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
            assertThat(result.getContent().get(0).getTitle()).isEqualTo("Title 1");
            assertThat(result.getContent().get(1).getId()).isEqualTo(2L);
            assertThat(result.getContent().get(1).getTitle()).isEqualTo("Title 2");
        }

        @Test
        void getDetailPost() {
            // given
            Long postId = 1L;
            Post post = new Post(postId, "Title", "Content", new User());
            when(postRepository.findById(postId)).thenReturn(java.util.Optional.of(post));

            // when
            DetailPostResponseDto result = postService.getDetailPost(postId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(postId);
            assertThat(result.getTitle()).isEqualTo("Title");
            assertThat(result.getContent()).isEqualTo("Content");
        }

        @Test
        void editPost() {
            // given
            Long postId = 1L;
            User user = new User();
            CreatePostRequestDto requestDto = new CreatePostRequestDto("newTitle", "newContent");
            Post existingPost = new Post(postId, "oldTitle", "oldContent", user);
            when(postRepository.findByIdAndUser(postId, user)).thenReturn(java.util.Optional.of(existingPost));
            when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // when
            postService.editPost(postId, requestDto, user);
            // then
            assertThat(existingPost.getTitle()).isEqualTo("newTitle");
            assertThat(existingPost.getContent()).isEqualTo("newContent");
            verify(postRepository).save(existingPost);
        }

        @Test
        void deletePost() {
            // given
            Long postId = 1L;
            User user = new User();
            when(postRepository.findByIdAndUser(postId, user)).thenReturn(java.util.Optional.of(new Post()));

            // when
            postService.deletePost(postId, user);
            // then
            verify(postRepository).deleteById(postId);
        }
    } // 성공 케이스
}