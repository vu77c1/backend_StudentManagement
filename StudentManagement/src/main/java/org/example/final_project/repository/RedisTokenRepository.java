package org.example.final_project.repository;

import org.example.final_project.model.auth.RedisToken;
import org.example.final_project.model.auth.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisTokenRepository extends CrudRepository<RedisToken,String> {
}
