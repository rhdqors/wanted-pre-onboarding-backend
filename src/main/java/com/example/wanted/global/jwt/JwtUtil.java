package com.example.wanted.global.jwt;

import com.example.wanted.domain.user.entity.UserRoleEnum;
import com.example.wanted.domain.user.security.UserDetailsServiceImpl;
import com.example.wanted.global.exception.GlobalException;
import com.example.wanted.global.exception.GlobalErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;


@Component
@RequiredArgsConstructor
@Slf4j
// 토큰 생성 및 유효성 검사 -> 들어오는 요청 처리 필요X
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";     //http요청 응답 보낼 때, 헤더값 key에 해당.
    public static final String AUTHORIZATION_KEY = "role";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L; // 1시간
//    private static final long TOKEN_TIME = 60 * 1000L; // 테스트용 30초

    private final UserDetailsServiceImpl userDetailsService;       //스프링 시큐리티

    // application.yml 파일에 정의된 JWT secret key
    @Value("${jwt.secret.key}")
    private String secretKey;

    // JWT secret key를 Key 타입으로 변환하여 저장
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // secretKey를 Base64 디코딩하여 Key 타입으로 변환
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 전달된 HttpServletRequest 객체에서 "Authorization" 헤더의 값에서 토큰을 추출하여 반환
    public String resolveToken(HttpServletRequest request) {
        // header 토큰을 가져오기
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        log.info("bearerToken === "  + bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 유저 토큰 생성
    public String createToken(String email, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(email)
                .claim(AUTHORIZATION_KEY, role)
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // token 에서 유저 정보 가져오기
    public Claims authorizeSocketToken(String token) {

        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            token = token.substring(7);
        }
        Claims claims;

        if (token != null) {
            if (validateToken(token)) {
                claims = getUserInfoFromToken(token);
                return claims;
            } else
                throw new GlobalException(GlobalErrorCode.INVALID_TOKEN);
        }
        return null;
    }

    // 토큰에서 사용자 정보 가져오기          //시큐리티
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String email, String role) {
        UserDetails userDetails = null;
        if(Objects.equals(role, "USER")){
            userDetails = userDetailsService.loadUserByUsername(email);
        }

        assert userDetails != null : "UserDetails must not be null";
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
