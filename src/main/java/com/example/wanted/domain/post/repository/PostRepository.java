package com.example.wanted.domain.post.repository;

import com.example.wanted.domain.post.dto.response.AllPostResponseDto;
import com.example.wanted.domain.post.entity.Post;
import com.example.wanted.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {
    Page<AllPostResponseDto> findAllByOrderByIdAsc(Pageable pageable);

    Optional<Post> findByIdAndUser(Long id, User user);




}
