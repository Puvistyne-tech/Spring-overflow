package uge.fr.ugeoverflow.controller.thin;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uge.fr.ugeoverflow.service.TagService;
import uge.fr.ugeoverflow.utils.Popup;


@Controller
@RequestMapping(value = {"/"})

public class TagsController {

    private final TagService tagService;

    public TagsController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public String getTags(Model model, Popup popup) {
        var tags = tagService.getAllTagsQuestion();
        model.addAttribute("popup", popup);
        model.addAttribute("tags", tags);
        return "tags";
    }

    @PostMapping("/tags")
    public String postTags() {
        return "tags";
    }

    @GetMapping("/tags/{tagType}")
    public String getTagsByType(@PathVariable("tagType") String tagType, Model model) {
        return "redirect:/search?q=%5B" + tagType + "%5D";
    }
}

