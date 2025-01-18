package com.example.movieAPI.auth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Builder
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenId;

    @Column(nullable = false,length = 100)
    @NotBlank(message = "Please enter refresh token value")
    private String refreshToken;

    @Column(nullable = false)
    private Instant expiryTime;

    @OneToOne
    private User user;

    public RefreshToken(Integer tokenId, String refreshToken, Instant expiryTime, User user) {
        this.tokenId = tokenId;
        this.refreshToken = refreshToken;
        this.expiryTime = expiryTime;
        this.user = user;
    }

    public RefreshToken() {
    }

}
