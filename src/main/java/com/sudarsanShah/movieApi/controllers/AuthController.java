package com.sudarsanShah.movieApi.controllers;

import com.sudarsanShah.movieApi.auth.models.RefreshToken;
import com.sudarsanShah.movieApi.auth.models.User;
import com.sudarsanShah.movieApi.auth.services.JwtUtilService;
import com.sudarsanShah.movieApi.auth.services.RefreshTokenService;
import com.sudarsanShah.movieApi.auth.services.UserAuthService;
import com.sudarsanShah.movieApi.auth.utils.AuthResponse;
import com.sudarsanShah.movieApi.auth.utils.LoginRequest;
import com.sudarsanShah.movieApi.auth.utils.RefreshTokenRequest;
import com.sudarsanShah.movieApi.auth.utils.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private JwtUtilService jwtUtilService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userAuthService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userAuthService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
        User user = refreshToken.getUser();
        String accessToken = jwtUtilService.generateToken(user);
        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build());
    }
}
