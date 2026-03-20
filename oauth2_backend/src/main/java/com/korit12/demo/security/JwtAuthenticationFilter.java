package com.korit12.demo.security;

import com.korit12.demo.repository.UserRepository;
import com.korit12.demo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        String token = request.getHeader("Authorization");

        // 🚨 [여기가 핵심!] 토큰이 null이거나 비어있으면, 검증을 시도하지 않고 바로 다음 필터로 넘깁니다.
        if (token == null || token.trim().isEmpty()) {
            filterChain.doFilter(request, response);
            return; // 메서드 종료
        }

        try {
            // 2. 토큰이 있을 때만 검증을 시도합니다. (기존 작성하신 코드)
            if (jwtService.validateToken(token)) {
                // ... Authentication 객체 생성 및 SecurityContextHolder에 저장 로직 ...
            }
        } catch (Exception e) {
            // 토큰이 유효하지 않은 경우의 예외 처리 (필요시)
            System.out.println("JWT 검증 실패: " + e.getMessage());
        }

        // 3. 다음 필터로 이동
        filterChain.doFilter(request, response);

        String email=jwtService.extractEmail(token);
        String role=jwtService.extractRole(token);

        userRepository.findByEmail(email).ifPresent(user -> {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    email,null, Collections.singleton(new SimpleGrantedAuthority(role))
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        });
    }
}
