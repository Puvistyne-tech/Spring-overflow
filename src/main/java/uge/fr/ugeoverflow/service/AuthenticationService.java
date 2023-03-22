package uge.fr.ugeoverflow.service;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import uge.fr.ugeoverflow.model.User;
import uge.fr.ugeoverflow.security.UserRole;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class AuthenticationService {

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    public AuthenticationService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(User user) {

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuedAt(new Date().toInstant())
                .expiresAt(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)).toInstant())
                .subject(user.getUsername())
                .claim("scope", UserRole.valueOf(user.getRole()).getAuthorityString())
                .build();
        var params = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claimsSet);
        var token = jwtEncoder.encode(params).getTokenValue();
//        user.setToken(token);
        return token;
    }

    public boolean isTokenDead(String token) {
        try {
            var jwt = jwtDecoder.decode(token);
            return Objects.requireNonNull(jwt.getExpiresAt()).isBefore(Instant.now());
        } catch (JwtException e) {
            return true;
        }
    }


}
