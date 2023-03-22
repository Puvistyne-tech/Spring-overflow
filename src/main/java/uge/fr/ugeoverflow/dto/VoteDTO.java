package uge.fr.ugeoverflow.dto;

import uge.fr.ugeoverflow.dto.profile.UserBoxDTO;
import uge.fr.ugeoverflow.model.VOTE_TYPE;
import uge.fr.ugeoverflow.model.Vote;

public class VoteDTO {
    private VOTE_TYPE vote;
    private UserBoxDTO user;

    public VoteDTO() {
    }

    public VoteDTO(Vote vote) {
        this.vote = vote.getVoteType();
        this.user = UserBoxDTO.fromUser(vote.getUser());
    }

    public VOTE_TYPE getVote() {
        return vote;
    }

    public boolean isUpvote() {
        return vote == VOTE_TYPE.UPVOTE;
    }

    public boolean isDownvote() {
        return vote == VOTE_TYPE.DOWNVOTE;
    }

    public void setVote(VOTE_TYPE vote) {
        this.vote = vote;
    }

    public UserBoxDTO getUser() {
        return user;
    }

    public void setUser(UserBoxDTO user) {
        this.user = user;
    }
}