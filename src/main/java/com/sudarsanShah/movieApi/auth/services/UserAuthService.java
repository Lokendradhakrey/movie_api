package com.sudarsanShah.movieApi.auth.services;

import com.sudarsanShah.movieApi.auth.models.User;
import com.sudarsanShah.movieApi.auth.models.UserRole;
import com.sudarsanShah.movieApi.auth.repositories.UserRepo;
import com.sudarsanShah.movieApi.auth.utils.AuthResponse;
import com.sudarsanShah.movieApi.auth.utils.LoginRequest;
import com.sudarsanShah.movieApi.auth.utils.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtUtilService jwtUtilService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .userRole(UserRole.USER)
                .build();
        User savedUser = userRepo.save(user);
        var accessToken = jwtUtilService.generateToken(savedUser);
        var refreshToken = refreshTokenService.createRefreshToken(savedUser.getEmail());
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        var user = userRepo.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found "));
        var accessToken = jwtUtilService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(loginRequest.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }
}
