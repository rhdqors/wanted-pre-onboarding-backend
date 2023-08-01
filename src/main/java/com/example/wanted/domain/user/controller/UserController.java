package com.example.wanted.domain.user.controller;

import com.example.wanted.domain.user.dto.request.LoginRequestDto;
import com.example.wanted.domain.user.dto.request.SignupRequestDto;
import com.example.wanted.domain.user.service.UserService;
import com.example.wanted.global.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage<>(HttpStatus.BAD_REQUEST.value(),bindingResult.getFieldError().getDefaultMessage(), ""));
        }
        return ResponseMessage.SuccessResponse(userService.signup(signupRequestDto), "");
    }

    // 로그인
    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return ResponseMessage.SuccessResponse(userService.login(loginRequestDto, response), "");
    }


}
