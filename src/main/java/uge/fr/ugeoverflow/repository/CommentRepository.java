package uge.fr.ugeoverflow.repository;

import org.springframework.data.repository.CrudRepository;
import uge.fr.ugeoverflow.model.Comment;
import uge.fr.ugeoverflow.model.Question;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public interface CommentRepository extends CrudRepository<Comment, UUID> {

    List<Comment> findAll();

    List<Comment> findAllByOverflowId(UUID overflowId);
//    List<Comment> findAllByAnswerId(UUID answerId);
//
//    List<Comment> findAllByUserId(UUID userId);

}
