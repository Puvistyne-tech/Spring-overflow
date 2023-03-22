package uge.fr.ugeoverflow.filters;

import uge.fr.ugeoverflow.dto.question.OneQuestionDTO;
import uge.fr.ugeoverflow.dto.question.QuestionDTO;
import uge.fr.ugeoverflow.repository.QuestionRepository;

import java.util.List;
import java.util.stream.Collectors;

public record NewestQuestionsFilterStrategy(
        QuestionRepository questionRepository) implements QuestionFilterStrategy {

    @Override
    public QuestionFilterType getFilterName() {
        return QuestionFilterType.NEWEST;
    }

    @Override
    public List<QuestionDTO> filterQuestions() {
        return questionRepository.findAllByOrderByCreationTimeDesc().stream().map(QuestionDTO::new).collect(Collectors.toList());

    }
}
