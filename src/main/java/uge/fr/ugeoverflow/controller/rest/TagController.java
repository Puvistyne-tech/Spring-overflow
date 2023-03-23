package uge.fr.ugeoverflow.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uge.fr.ugeoverflow.dto.question.QuestionDTO;
import uge.fr.ugeoverflow.dto.tag.TagsDTO;
import uge.fr.ugeoverflow.model.Tag;
import uge.fr.ugeoverflow.repository.TagRepository;
import uge.fr.ugeoverflow.service.TagService;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "/api/v1/tags")
public class TagController {

    private final TagRepository tagRepository;

    private final TagService tagService;

    public TagController(TagRepository tagRepository, TagService tagService) {
        this.tagRepository = tagRepository;
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag.TAG_TYPE> getAllTags() {

        var it = tagRepository.findAll();

        var toList = StreamSupport.stream(it.spliterator(), false).map(Tag::getTagType).toList();

        return toList;
    }

    @GetMapping("/{tagType}")
    public ResponseEntity<List<QuestionDTO>> getTagWithQuestions(@PathVariable("tagType") String tagType) {
        var tags = tagService.getTagsWithQuestions(tagType);
        return ResponseEntity.ok(tags);
    }


}
