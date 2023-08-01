package com.example.wanted.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @Size(min = 8, message = "8자리 이상 비밀번호를 설정하세요")
    private String password;
}
