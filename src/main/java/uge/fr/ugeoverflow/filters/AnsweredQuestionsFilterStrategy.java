package uge.fr.ugeoverflow.filters;

import uge.fr.ugeoverflow.dto.question.OneQuestionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uge.fr.ugeoverflow.dto.question.OneQuestionDTO;
import uge.fr.ugeoverflow.dto.question.QuestionDTO;
import uge.fr.ugeoverflow.repository.QuestionRepository;

import java.util.List;
import java.util.stream.Collectors;

public record AnsweredQuestionsFilterStrategy(QuestionRepository questionRepository) implements QuestionFilterStrategy {


    @Override
    public QuestionFilterType getFilterName() {
        return QuestionFilterType.ANSWERED;
    }

    @Override
    public List<QuestionDTO> filterQuestions() {
        return questionRepository.findAllAnsweredQuestions().stream().map(QuestionDTO::new).collect(Collectors.toList());
    }
}
