package com.sudarsanShah.movieApi.auth.repositories;

import com.sudarsanShah.movieApi.auth.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByRefreshToken(String token);
}
