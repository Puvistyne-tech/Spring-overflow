package uge.fr.ugeoverflow.model;

import jakarta.persistence.*;


@Entity
@Table(name = "votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private VOTE_TYPE voteType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Vote() {
    }

    public Vote(VOTE_TYPE voteType, User user) {
        this.voteType = voteType;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VOTE_TYPE getVoteType() {
        return voteType;
    }

    public void setVoteType(VOTE_TYPE voteType) {
        this.voteType = voteType;
    }

}