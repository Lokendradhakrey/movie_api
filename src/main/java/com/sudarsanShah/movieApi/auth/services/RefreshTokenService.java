package com.sudarsanShah.movieApi.auth.services;

import com.sudarsanShah.movieApi.auth.models.RefreshToken;
import com.sudarsanShah.movieApi.auth.models.User;
import com.sudarsanShah.movieApi.auth.repositories.RefreshTokenRepo;
import com.sudarsanShah.movieApi.auth.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    public RefreshToken createRefreshToken(String username) {
        User user = userRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        RefreshToken refreshToken = user.getRefreshToken();
        if (refreshToken == null) {
            long refreshTokenValidity = 60 * 1000;
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user)
                    .build();
            refreshTokenRepo.save(refreshToken);
        }
        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken refToken = refreshTokenRepo.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        if (refToken.getExpirationTime().compareTo(Instant.now()) < 0) {
            refreshTokenRepo.delete(refToken);
            throw new RuntimeException("Refreshed token expired");
        }

        return refToken;
    }
}
