package uge.fr.ugeoverflow.service;

import org.springframework.stereotype.Service;
import uge.fr.ugeoverflow.model.TokenExpired;
import uge.fr.ugeoverflow.repository.TokenExpiredRepository;

@Service
public class TokenExpiredService {

    private final TokenExpiredRepository tokenExpiredRepository;

    public TokenExpiredService(TokenExpiredRepository tokenExpiredRepository) {
        this.tokenExpiredRepository = tokenExpiredRepository;
    }

    public boolean isTokenExpired(String token) {
        return tokenExpiredRepository.existsByToken(token);
    }

    public void addTokenExpired(String token) {
        tokenExpiredRepository.save(new TokenExpired(token));
    }


}
