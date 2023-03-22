package uge.fr.ugeoverflow.filters;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uge.fr.ugeoverflow.dto.question.OneQuestionDTO;
import uge.fr.ugeoverflow.dto.question.QuestionDTO;

import java.util.List;

public interface QuestionFilterStrategy {

    QuestionFilterType getFilterName();

    List<QuestionDTO> filterQuestions();

}
