package com.example.wanted.domain.user.service;

import com.example.wanted.domain.user.dto.request.LoginRequestDto;
import com.example.wanted.domain.user.dto.request.SignupRequestDto;
import com.example.wanted.domain.user.entity.User;
import com.example.wanted.domain.user.entity.UserRoleEnum;
import com.example.wanted.domain.user.repository.UserRepository;
import com.example.wanted.global.exception.GlobalErrorCode;
import com.example.wanted.global.exception.GlobalException;
import com.example.wanted.global.jwt.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private HttpServletResponse response;

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCase {
        @Test
        void signup() {
            // Given
            String email = "test@1.com";
            String password = "test";
            SignupRequestDto signupRequestDto = new SignupRequestDto(email, password);
            User user = new User(signupRequestDto, password, UserRoleEnum.USER);

            given(passwordEncoder.encode(password)).willReturn("비밀번호 암호화");
            given(userRepository.existsByEmail(email)).willReturn(false);
            given(userRepository.save(any(User.class))).willReturn(user);

            // When
            String result = userService.signup(signupRequestDto);

            // Then, 검증
            assertEquals("회원가입 완료", result);
            verify(passwordEncoder).encode(password);
            verify(userRepository).existsByEmail(email);
            verify(userRepository).save(any(User.class));
        }

        @Test
        void login() {
            // given
            String email = "test@1.com";
            String password = "test";
            String encodedPassword = "encodedPassword";
            String token = "testToken";

            LoginRequestDto loginRequestDto = new LoginRequestDto(email, password);
            User user = new User();
            user.setPassword(encodedPassword);

            given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
            given(passwordEncoder.matches(password, encodedPassword)).willReturn(true);
            given(jwtUtil.createToken(email, UserRoleEnum.USER)).willReturn(token);

            // when
            String result = userService.login(loginRequestDto, response);
            // Then
            assertEquals("로그인 완료", result);

            // 검증
            verify(userRepository).findByEmail(email);
            verify(passwordEncoder).matches(password, encodedPassword);
            verify(jwtUtil).createToken(email, UserRoleEnum.USER);
            verify(response).addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        }
    } // 성공 케이스

    @Nested
    @DisplayName("실패 케이스")
    class FailCase {
        @Test
        void duplicateEmail() {
            String email = "test@1.com";
            given(userRepository.existsByEmail(email)).willReturn(true);

            // when & then
            try {
                userService.emailCheck(email);
                fail("error: " + GlobalErrorCode.DUPLICATE_EMAIL);
            } catch (GlobalException ex) {
                assertEquals(GlobalErrorCode.DUPLICATE_EMAIL, ex.getErrorCode());
            }
        }

        @Test
        void passwordMissmatch() {
            // given
            String email = "test@1.com";
            String password = "inputPassword";
            LoginRequestDto loginRequestDto = new LoginRequestDto(email, password);
            User user = new User();
            given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
            given(passwordEncoder.matches(password, user.getPassword())).willReturn(false);
            // when & then
            Assertions.assertThrows(GlobalException.class,
                    () -> userService.login(loginRequestDto, response), GlobalErrorCode.PASSWORD_MISMATCH.getMessage());
        }
    } // 실패 케이스
}