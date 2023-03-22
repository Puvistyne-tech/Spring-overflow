package uge.fr.ugeoverflow.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class FollowRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_id")
    private User followed;

    private int reputation = 5;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public FollowRelationship() {
    }

    public FollowRelationship(User follower, User followed) {
        this.follower = follower;
        this.followed = followed;
    }

    public FollowRelationship(User follower, User followed, int reputation) {
        this.follower = follower;
        this.followed = followed;
        this.reputation = reputation;
    }


    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowed() {
        return followed;
    }

    public void setFollowed(User followed) {
        this.followed = followed;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public void setReputationScore(int reputation) {
        this.reputation = reputation;
    }

    public static int getReputationScore(FollowRelationship relationship) {
        return relationship.getReputation();
    }

    // equals() and hashCode() methods based on follower and followed
}