package com.example.wanted.domain.user.service;

import com.example.wanted.domain.user.dto.request.LoginRequestDto;
import com.example.wanted.domain.user.dto.request.SignupRequestDto;
import com.example.wanted.domain.user.entity.User;
import com.example.wanted.domain.user.entity.UserRoleEnum;
import com.example.wanted.domain.user.repository.UserRepository;
import com.example.wanted.global.exception.GlobalErrorCode;
import com.example.wanted.global.exception.GlobalException;
import com.example.wanted.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 회원가입
    @Transactional
    public String signup(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 이메일 중복 검사 -> 없다면 유저 저장
        emailCheck(email);
        userRepository.save(new User(signupRequestDto, password, UserRoleEnum.USER));

        return "회원가입 완료";
    }

    // 로그인
    @Transactional
    public String login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        // 이메일 존재, 일치 여부
        User user = findUser(email);
        // 비밀번호 일치 여부
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new GlobalException(GlobalErrorCode.PASSWORD_MISMATCH);
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(email, UserRoleEnum.USER));
        return "로그인 완료";
    }

    // 이메일 중복 검사 메서드
    public void emailCheck(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new GlobalException(GlobalErrorCode.DUPLICATE_EMAIL);
        }
    }

    // 이메일로 유저 찾기
    public User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.EMAIL_NOT_FOUND));
    }
}
