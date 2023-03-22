package uge.fr.ugeoverflow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import uge.fr.ugeoverflow.dto.question.QuestionDTO;
import uge.fr.ugeoverflow.model.Question;
import uge.fr.ugeoverflow.model.User;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends CrudRepository<Question, UUID> , PagingAndSortingRepository<Question, UUID> {

    List<Question> findAll();

    List<Question> findAllByUserId(UUID userId);

    List<Question> findAllQuestionsByUser(User user);

    boolean existsByTitle(String title);

    boolean existsById(UUID questionId);

    //the answers and tags will be fetched too when calling the findAll Pageable method, this will not have a negative impact on the performance
    // because we are not fetching large data (only 10 elements by page )
    @EntityGraph(attributePaths = {"answers", "tags"}, type = EntityGraph.EntityGraphType.FETCH)
    Page<Question> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"answers", "tags"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Question> findAllByOrderByCreationTimeDesc();

    @EntityGraph(attributePaths = {"answers", "tags"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT q FROM Question q WHERE q.answers IS EMPTY")
    List<Question> findAllUnansweredQuestions();

    @EntityGraph(attributePaths = {"answers", "tags"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT q FROM Question q WHERE SIZE(q.answers) > 0")
    List<Question> findAllAnsweredQuestions();

    @EntityGraph(attributePaths = {"answers", "tags"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Question> findAllByOrderByCreationTimeAsc();

    @Query("SELECT q FROM Question q JOIN q.user u WHERE u IN " +
            "(SELECT f.followed FROM FollowRelationship f WHERE f.follower = :user)")
    List<Question> findQuestionsOfUsersFollowedByUser(@Param("user") User user);

    @Query("SELECT q FROM Question q WHERE q.user IN " +
            "(SELECT fr2.followed FROM FollowRelationship fr2 WHERE fr2.follower IN " +
            "(SELECT fr.followed FROM FollowRelationship fr WHERE fr.follower = :user " +
            "OR fr.follower IN (SELECT fr3.followed FROM FollowRelationship fr3 WHERE fr3.follower = :user)))")
    List<Question> findQuestionsOfUsersFollowedByUsersFollowedByUser(@Param("user") User user);

    boolean existsByUserIdIn(List<UUID> userIds);


}