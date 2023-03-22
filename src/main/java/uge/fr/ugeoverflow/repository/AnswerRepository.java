package uge.fr.ugeoverflow.repository;

import org.springframework.data.repository.CrudRepository;
import uge.fr.ugeoverflow.model.Answer;
import uge.fr.ugeoverflow.model.Question;
import uge.fr.ugeoverflow.model.User;
import uge.fr.ugeoverflow.model.Vote;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnswerRepository extends CrudRepository<Answer, UUID> {

    List<Answer> findAll();
    List<Answer> findAllByQuestionId(UUID questionId);

    List<Answer> findAllByUserId(UUID userId);

    boolean existsById(UUID answerId);

    List<Answer> findByQuestion(Question question);

//    List<Answer> findAllByUserId(User user);






}
