package uge.fr.ugeoverflow.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uge.fr.ugeoverflow.model.FollowRelationship;
import uge.fr.ugeoverflow.model.User;

import java.util.List;
import java.util.UUID;

public interface FollowRelationshipRepository extends CrudRepository<FollowRelationship, UUID> {
    void deleteByFollowerAndFollowed(User follower, User followed);

    FollowRelationship findFollowRelationshipByFollowerAndFollowed(User follower, User followed);

    List<FollowRelationship> findByFollowed(User followed);

    List<FollowRelationship> findByFollower(User follower);

    List<FollowRelationship> findByFollowerAndFollowedIn(User follower, List<User> followed);

    List<FollowRelationship> findByFollowerInAndFollowed(List<User> follower, User followed);

    boolean existsByFollowerAndFollowed(User follower, User followed);

    @Query("SELECT fr.followed.id FROM FollowRelationship fr WHERE fr.follower.id = :userId")
    List<UUID> findAllFollowedUsersByGivenUser(UUID userId);
}