package kr.pe.karsei.springbootjwtstudy.controllers;

import kr.pe.karsei.springbootjwtstudy.models.TokenResponse;
import kr.pe.karsei.springbootjwtstudy.services.AuthUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "auth")
public class AuthController {
    private final AuthUserService authUserService;
    public AuthController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> params) {
        // 인증 및 토큰 생성
        TokenResponse token = authUserService.authorize(params.get("username"));

        return ResponseEntity.ok(token);
    }
}
