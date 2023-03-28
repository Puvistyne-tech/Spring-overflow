package uge.fr.ugeoverflow.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uge.fr.ugeoverflow.error.image.ImageNotFoundException;
import uge.fr.ugeoverflow.model.Image;
import uge.fr.ugeoverflow.repository.ImageRepository;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void deleteImage(UUID id) {
        imageRepository.deleteById(id);
    }

    public Image uploadImage(MultipartFile image) throws IOException {
        Image img = new Image();
        image.getContentType();
        img.setData(image.getBytes());
        img.setName(image.getOriginalFilename());
        var imageFound = imageRepository.findByName(image.getOriginalFilename());
        return imageFound.orElseGet(() -> imageRepository.save(img));
    }

    public Image uploadImage(byte[] image, String name) throws IOException {
        Image img = new Image();
        img.setData(image);
        img.setName(name);
        var imageFound = imageRepository.findByName(name);
        return imageFound.orElseGet(() -> imageRepository.save(img));
    }


    public Image getImageByID(UUID id) {
        return imageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid image Id:" + id));
    }

    public String getImageUrl(Image image) {
        return "http://localhost:8080/images/id/" + image.getId();
    }

    public Image getImageByName(String name) {
        return imageRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException("Invalid image name:" + name));
    }
    public Image getImageByNameContains(String name) {
        return imageRepository.findFirstByNameContains(name).orElseThrow(() -> new IllegalArgumentException("Invalid image name:" + name));
    }
}
