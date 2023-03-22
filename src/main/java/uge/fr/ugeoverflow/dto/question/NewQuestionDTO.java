package uge.fr.ugeoverflow.dto.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class NewQuestionDTO {

    @NotBlank(message = "{NotBlank.NewQuestionDTO.Title}")
    @Size(min = 10, max = 100, message = "{Size.NewQuestionDTO.Title}")
    @Pattern(regexp = "^[a-zA-Z].*$", message = "{Pattern.NewQuestionDTO.Title}")
    public String title;

    @NotBlank(message = "{NotBlank.NewQuestionDTO.Body}")
    @Size(min = 20, max = 10000, message = "{Size.NewQuestionDTO.Body}")
    public String body;

    @NotEmpty(message = "{NotEmpty.NewQuestionDTO.Tags}")
    public List<String> tags = new ArrayList<>();
    public List<MultipartFile> images;

    public NewQuestionDTO() {
    }

    public NewQuestionDTO(String title, String body, List<String> tags, List<MultipartFile> images) {
        this.title = title;
        this.body = body;
        this.tags = tags;
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

}
