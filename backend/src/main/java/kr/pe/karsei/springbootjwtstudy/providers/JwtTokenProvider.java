package kr.pe.karsei.springbootjwtstudy.providers;

import io.jsonwebtoken.*;
import kr.pe.karsei.springbootjwtstudy.models.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    // JWT Secret Key
    @Value("${jwt.secret-key}")
    private String secretKey;

    // Access Token 유효시간 (30분)
    private static final long TIME_VALID_ACCEES_TOKEN = 30 * 60 * 1000L;

    // Refresh Token 유효시간
    private static final long TIME_VALID_REFRESH_TOKEN = 60 * 60 * 1000L;

    /**
     * 생성자를 만들면서 미리 Secret Key 를 Base64 로 변환합니다.
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * JWT Access Token 을 생성합니다.
     * @param user 유저 정보
     * @return Access Token 문자열
     */
    public String createAccessToken(AuthUser user) {
        Date now = new Date();

        // Payload
        Claims claims = Jwts.claims()
                // sub - Subject (whom the token refers to)
                .setSubject(user.getUsername())
                // iss - Issuer (who created and signed this token)
                .setIssuer("SampleApi")
                // jti - JWT ID (unique identifier for this token)
                .setId(user.getUsername())
                // iat - issued at (seconds since Unix epoch)
                .setIssuedAt(now)
                // exp - Expiration time (seconds since Unix epoch)
                .setExpiration(new Date(now.getTime() + TIME_VALID_ACCEES_TOKEN));
        claims.put("roles", user.getAuthorities());

        return Jwts.builder()
                // [Header] typ - Type of token
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                // [Payload]
                .setClaims(claims)
                // [Signature] alg - Signature or encryption algorithm
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * JWT Refresh Token 을 생성합니다.
     * @return Refresh Token 문자열
     */
    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TIME_VALID_REFRESH_TOKEN))
                .compact();
    }

    /**
     * JWT Token 정보를 조회합니다.
     * @param token JWT Token
     * @return JWT Token 정보 객체
     */
    public Claims getClaims(String token) {
        /*
         * {
         *   "sub": "developer",
         *   "iss": "SampleApi",
         *   "jti": "developer",
         *   "roles": [],
         *   "iat": 1650171240,
         *   "exp": 1650173040
         * }
         */
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    /**
     * HTTP Request 의 Header 에서 JWT Token 값을 가져옵니다.
     * @param request HTTP 요청 객체
     * @return JWT Token 문자열
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    /**
     * JWT Token 을 검증하여 Token 이 유효한지 판단합니다.
     * @param token JWT 토큰
     * @return 유효하다면 {@code true}; 그렇지 않다면 {@code false}
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        }
        catch (SignatureException e) {
            log.error("Invalid JWT signatrue: {}", e.getMessage());
        }
        catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        }
        catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        }
        catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        }
        catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
