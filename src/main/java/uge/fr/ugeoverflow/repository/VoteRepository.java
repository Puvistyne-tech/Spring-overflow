package uge.fr.ugeoverflow.repository;

import org.springframework.data.repository.CrudRepository;
import uge.fr.ugeoverflow.model.Answer;
import uge.fr.ugeoverflow.model.User;
import uge.fr.ugeoverflow.model.Vote;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VoteRepository extends CrudRepository<Vote, Long> {

    //    List<Vote> findByUserAndAnswer(User user, Answer answer);
    Optional<Vote> findByUser(User user);


    //    Iterable<Vote> findByAnswer(Answer answer);

}