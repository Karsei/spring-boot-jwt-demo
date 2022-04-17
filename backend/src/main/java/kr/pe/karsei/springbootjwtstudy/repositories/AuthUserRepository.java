package kr.pe.karsei.springbootjwtstudy.repositories;

import kr.pe.karsei.springbootjwtstudy.models.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    /**
     * 이름으로 특정 유저의 정보를 조회합니다.
     * @param username 유저 이름
     * @return 유저 객체
     */
    Optional<AuthUser> findByName(String username);
}
