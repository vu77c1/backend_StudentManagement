package org.example.final_project.service.auth;

import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.exception.InvalidDataException;
import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.model.auth.Token;
import org.example.final_project.repository.TokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record TokenService(TokenRepository tokenRepository) {

    /**
     * Get token by username
     *
     * @param username
     * @return token
     */
    public Token getByUsername(String username) {
        return tokenRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Not found token"));
    }

    /**
     * Save token to DB
     *
     * @param token
     * @return
     */
    public int save(Token token) {
        Optional<Token> optional = tokenRepository.findByUsername(token.getUsername());
        if (optional.isEmpty()) {
            tokenRepository.save(token);
            return token.getId();
        } else {
            Token t = optional.get();
            t.setAccessToken(token.getAccessToken());
            t.setRefreshToken(token.getRefreshToken());
            tokenRepository.save(t);
            return t.getId();
        }
    }

    /**
     * Delete token by username
     *
     * @param username
     */
    public void delete(String username) {
        Token token = getByUsername(username);
        tokenRepository.delete(token);
    }

    public boolean isExists(String username) {
        if (!tokenRepository.existsByUsername(username)) {
            throw new InvalidDataException("Token not exists");
        }
        return true;
    }

    public Optional<Token> findByAccessToken(String token) {
        return tokenRepository.findByAccessToken(token);
    }
}