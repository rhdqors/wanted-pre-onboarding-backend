package com.example.wanted.domain.user.entity;

import com.example.wanted.domain.user.dto.request.SignupRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public User(SignupRequestDto signupRequestDto, String password, UserRoleEnum role) {
        this.email = signupRequestDto.getEmail();
        this.password = password;
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
