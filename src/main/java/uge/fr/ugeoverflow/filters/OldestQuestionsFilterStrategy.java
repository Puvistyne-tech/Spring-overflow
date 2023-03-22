package uge.fr.ugeoverflow.filters;

import uge.fr.ugeoverflow.dto.question.OneQuestionDTO;
import uge.fr.ugeoverflow.dto.question.QuestionDTO;
import uge.fr.ugeoverflow.repository.QuestionRepository;

import java.util.List;
import java.util.stream.Collectors;

public record OldestQuestionsFilterStrategy(QuestionRepository questionRepository) implements QuestionFilterStrategy{

    @Override
    public QuestionFilterType getFilterName() {
        return QuestionFilterType.OLDEST;
    }

    @Override
    public List<QuestionDTO> filterQuestions() {
        return questionRepository.findAllByOrderByCreationTimeAsc().stream().map(QuestionDTO::new).collect(Collectors.toList());
    }
}
