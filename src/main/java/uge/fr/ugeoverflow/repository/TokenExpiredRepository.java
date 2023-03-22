package uge.fr.ugeoverflow.repository;

import org.springframework.data.repository.CrudRepository;
import uge.fr.ugeoverflow.model.TokenExpired;

import java.util.UUID;

public interface TokenExpiredRepository extends CrudRepository<TokenExpired, UUID> {
    boolean existsByToken(String token);
}