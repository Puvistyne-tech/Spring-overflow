package uge.fr.ugeoverflow.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uge.fr.ugeoverflow.model.Tag;
import uge.fr.ugeoverflow.repository.TagRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "/api/v1/tags")
public class TagController {

    private final TagRepository tagRepository;

    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public List<Tag.TAG_TYPE> getAllTags() {

        var it= tagRepository.findAll();

        var toList= StreamSupport.stream(it.spliterator(),false).map(Tag::getTagType).toList();

        return toList;
    }

}
