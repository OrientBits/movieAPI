package com.example.movieAPI.auth.service;

import com.example.movieAPI.auth.entities.RefreshToken;
import com.example.movieAPI.auth.entities.User;
import com.example.movieAPI.auth.repositories.RefreshTokenRepository;
import com.example.movieAPI.auth.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }



    public RefreshToken createRefreshToken(String username){
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("user not found with the user name: " + username));

        RefreshToken refreshToken = user.getRefreshToken();

        if (refreshToken == null){
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expiryTime(Instant.now().plusMillis(5*60*60*1000))
                    .user(user)
                    .build();
        }

        return  refreshToken;
    }
}
