package uge.fr.ugeoverflow.controller.rest;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uge.fr.ugeoverflow.dto.TagDto;
import uge.fr.ugeoverflow.repository.TagRepository;
import uge.fr.ugeoverflow.service.TagService;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "/api/v1/tag")
public class TagsRestController {

    private final TagRepository tagRepository;
    private final TagService tagService;

    public TagsRestController(TagRepository tagRepository , TagService tagService) {
        this.tagService = tagService;
        this.tagRepository = tagRepository;
    }
    
    @GetMapping
    public List<TagDto> getAllTags() {
        var tags = tagService.getAllTagsQuestion();
        return tags;

    }
}
