package kr.pe.karsei.springbootjwtstudy.models;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenResponse {
    /**
     * Access Token
     */
    private String accessToken;

    /**
     * Refresh Token
     */
    private String refreshToken;
}
