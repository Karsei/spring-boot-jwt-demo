package kr.pe.karsei.springbootjwtstudy.services;

import kr.pe.karsei.springbootjwtstudy.models.AuthUser;
import kr.pe.karsei.springbootjwtstudy.models.TokenResponse;
import kr.pe.karsei.springbootjwtstudy.providers.JwtTokenProvider;
import kr.pe.karsei.springbootjwtstudy.repositories.AuthUserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AuthUserService implements UserDetailsService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthUserRepository authUserRepository;
    public AuthUserService(JwtTokenProvider jwtTokenProvider,
                           AuthUserRepository authUserRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authUserRepository = authUserRepository;
    }

    /**
     * 유저를 인증하고 JWT Token 을 생성합니다.
     * @param username 유저 이름
     * @return JWT Token 객체
     */
    public TokenResponse authorize(String username) {
        // 조회
        AuthUser member = loadUserByUsername(username);
        if (member == null) throw new IllegalArgumentException("가입되지 않은 이름입니다.");

        return TokenResponse.builder()
                .accessToken(jwtTokenProvider.createAccessToken(member))
                .refreshToken(jwtTokenProvider.createRefreshToken())
                .build();
    }

    /**
     * JWT Token 에서 인증 정보를 조회합니다.
     * @param token JWT 토큰
     * @return 인증 확인된 인증 객체 (setAuthenticated = true)
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = loadUserByUsername(jwtTokenProvider.getClaims(token).getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    /**
     * 유저 이름으로 특정 유저의 정보 객체를 조회합니다.
     * @param username 유저 이름
     * @return 유저 정보 객체
     * @throws UsernameNotFoundException 유저 데이터가 존재하지 않을 경우 예외 발생
     */
    @Override
    public AuthUser loadUserByUsername(String username) throws UsernameNotFoundException {
        String someId = "developer";
        if (someId.equals(username)) {
            return new AuthUser(1L, someId, new ArrayList<>());
        }
        else {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        //return authUserRepository.findByName(username)
        //        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}