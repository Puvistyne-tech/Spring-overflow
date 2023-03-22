package uge.fr.ugeoverflow.dto.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class NewAnswerDTO {

    @NotBlank(message = "{NotBlank.NewAnswerDTO.Body}")
    @Size(min = 1, max = 10000, message = "{Size.NewAnswerDTO.Body}")
    public String body;
    public List<MultipartFile> images;

    public NewAnswerDTO() {
    }

    public NewAnswerDTO(String body, List<MultipartFile> images) {
        this.body = body;
        this.images = images;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }



}
