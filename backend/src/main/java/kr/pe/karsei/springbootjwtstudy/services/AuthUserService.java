package kr.pe.karsei.springbootjwtstudy.services;

import kr.pe.karsei.springbootjwtstudy.models.AuthUser;
import kr.pe.karsei.springbootjwtstudy.repositories.AuthUserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AuthUserService implements UserDetailsService {
    private final AuthUserRepository authUserRepository;
    public AuthUserService(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
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