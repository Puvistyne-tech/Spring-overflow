package uge.fr.ugeoverflow.repository;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import uge.fr.ugeoverflow.dto.question.OneQuestionDTO;
import uge.fr.ugeoverflow.dto.question.QuestionDTO;
import uge.fr.ugeoverflow.model.Question;
import uge.fr.ugeoverflow.model.Tag;

import java.util.Optional;
import java.util.Set;

public interface TagRepository extends CrudRepository<Tag, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Tag> findByTagType(Tag.TAG_TYPE tagType);

//    @Query("SELECT COUNT(q) FROM Question q JOIN q.tags t WHERE t.tagType = :tagType")
//    int countQuestionsByTagType(@Param("tagType") Tag.TAG_TYPE tagType);

    @Query("SELECT q FROM Question q JOIN q.tags t WHERE t.tagType = :tagType")
    Set<QuestionDTO> findQuestionsByTagType(@Param("tagType") Tag.TAG_TYPE tagType);

    boolean existsByTagType(Tag.TAG_TYPE tagType);

//    List<Tag> saveAllIfNotExists(List<Tag> tag);

}