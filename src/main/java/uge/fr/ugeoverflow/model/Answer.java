package uge.fr.ugeoverflow.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "answer")
public class Answer extends Overflow {

    //ManyToOne is EAGER by default
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    public Question question;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Vote> votes;

    private int upVotes;
    private int downVotes;

    @Transient
    private final Object lock = new Object();


    public Answer() {
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }


    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }


    public void addVote(Vote vote) {
        if (votes == null)
            votes = new HashSet<>();
        votes.add(vote);
        synchronized (lock) {
            if (vote.getVoteType() == VOTE_TYPE.UPVOTE) {
                upVotes++;
            } else {
                downVotes++;
            }
        }
    }

    public void removeVote(Vote vote) {
        votes.remove(vote);
        synchronized (lock) {
            if (vote.getVoteType() == VOTE_TYPE.UPVOTE) {
                upVotes--;
            } else {
                downVotes--;
            }
        }
    }

    public int getScore() {
        return getUpVotes() - getDownVotes();
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }


    public int getUpVotes() {
        return upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public boolean isVoter(User user) {
        return votes.stream().anyMatch(vote -> vote.getUser().equals(user));
    }

}