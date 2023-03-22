package uge.fr.ugeoverflow.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "token_expired")
public class TokenExpired {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Lob
    private String token;

    public TokenExpired() {
    }

    public TokenExpired(String token) {
        this.token = token;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}