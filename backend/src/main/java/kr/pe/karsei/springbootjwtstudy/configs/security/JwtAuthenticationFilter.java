package kr.pe.karsei.springbootjwtstudy.configs.security;

import kr.pe.karsei.springbootjwtstudy.providers.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 가 유효한 Token 인지 인증하기 위한 Token 클래스
 * <p>Component 로 사용하면 Spring Security Filter 와 통합되므로 사용자가 정의한 필터를 사용하기 위해 {@link JwtTokenProvider} 를 통해서 인증 후 {@link SecurityContextHolder} 를 사용한다.</p>
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // HTTP Request 의 Header 에서 JWT Token 을 받아온다.
        String token = jwtTokenProvider.resolveToken(request);

        // 유효한 Token 인지 확인한다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // Token 이 유효하다면 Token 으로부터 유저의 정보를 가져온다.
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            // SecurityContext 에 Authentication 객체를 저장한다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
