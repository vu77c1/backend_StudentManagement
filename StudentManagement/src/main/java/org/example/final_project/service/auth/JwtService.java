package org.example.final_project.service.auth;

import org.example.final_project.util.TokenType;
import org.springframework.security.core.userdetails.UserDetails;


public interface JwtService {

    String generateToken(UserDetails user);

    String generateRefreshToken(UserDetails user);

    String generateResetToken(UserDetails user);

    String extractUsername(String token, TokenType type);

    boolean isValid(String token, TokenType type, UserDetails user);
}
