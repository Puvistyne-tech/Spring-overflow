package uge.fr.ugeoverflow.service;

import org.springframework.stereotype.Service;
import uge.fr.ugeoverflow.repository.FollowRelationshipRepository;

import java.util.List;
import java.util.UUID;

@Service
public class FollowRelationshipService {

    private final FollowRelationshipRepository followRelationshipRepository;

    public FollowRelationshipService(FollowRelationshipRepository followRelationshipRepository) {
        this.followRelationshipRepository = followRelationshipRepository;
    }

    public List<UUID> findAllFollowedUsersByGivenUser(UUID userId){
        return followRelationshipRepository.findAllFollowedUsersByGivenUser(userId);
    }


}
