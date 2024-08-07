package com.sudarsanShah.movieApi.auth.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "RefreshToken")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String refreshToken;
    private Instant expirationTime;
    @OneToOne
    private User user;
}
