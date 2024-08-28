package org.example.final_project.service.auth;

import lombok.RequiredArgsConstructor;
import org.example.final_project.exception.InvalidDataException;
import org.example.final_project.model.auth.RedisToken;
import org.example.final_project.repository.RedisTokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisTokenService {
    private final RedisTokenRepository redisTokenRepository;

    public void save(RedisToken token) {
        redisTokenRepository.save(token);
    }

    public void remove(String id) {
        isExists(id);
        redisTokenRepository.deleteById(id);
    }

    public boolean isExists(String id) {
        if (!redisTokenRepository.existsById(id)) {
            throw new InvalidDataException("Token not exists");
        }
        return true;
    }
}
