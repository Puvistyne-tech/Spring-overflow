package uge.fr.ugeoverflow.service;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uge.fr.ugeoverflow.dto.TagDto;
import uge.fr.ugeoverflow.dto.question.OneQuestionDTO;
import uge.fr.ugeoverflow.dto.question.QuestionDTO;
import uge.fr.ugeoverflow.model.Question;
import uge.fr.ugeoverflow.model.Tag;
import uge.fr.ugeoverflow.repository.TagRepository;
import uge.fr.ugeoverflow.utils.Utils;

import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    public void initialize() {
        var tags = Utils.createTags();
        assert tags != null;
        tags.forEach(tag -> {
            if (tagRepository.findByTagType(tag.getTagType()).isEmpty()) {
                tagRepository.save(tag);
            }
        });
    }

    //    @Transactional
    public List<TagDto> getAllTagsQuestion() {
        var tags = tagRepository.findAll();
        return StreamSupport.stream(tags.spliterator(), false).map(tag -> new TagDto(tag.getTagType().name(), tag.getDescription(), tag.getQuestionCount())).toList();
    }

    @Transactional
    public Set<QuestionDTO> getAllQuestionsByTagType(Tag.TAG_TYPE tagType) {
        return tagRepository.findQuestionsByTagType(tagType);
    }

    public boolean existsByTagType(Tag.TAG_TYPE tagType) {
        return tagRepository.existsByTagType(tagType);
    }


    @Transactional
    public List<QuestionDTO> getTagsWithQuestions(String tagType) {
        var tags = tagRepository.findQuestionsByTagType(Tag.TAG_TYPE.valueOf(tagType));
        return tags.stream().map(q -> {
                    var question = new QuestionDTO();
                    question.setId(q.getId());
                    question.setTitle(q.getTitle());
                    question.setBody(q.getBody());
                    question.setCreationTime(q.getCreationTime());
                    question.setUser(q.getUser());
                    question.setTags(q.getTags());
                    question.setTags(q.getTags());
                    question.setAnswersCounter(q.getAnswersCounter());
                    return question;
                }
        ).toList();
    }
}
