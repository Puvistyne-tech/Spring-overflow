package uge.fr.ugeoverflow.controller.rest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uge.fr.ugeoverflow.error.user.UserNotFoundException;
import uge.fr.ugeoverflow.model.Image;
import uge.fr.ugeoverflow.model.User;
import uge.fr.ugeoverflow.service.ImageService;
import uge.fr.ugeoverflow.service.UserService;
import uge.fr.ugeoverflow.utils.ResponseMessage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping({"/auth/images", "/images"})
public class ImageRestController {

    private final ImageService imageService;

    private final UserService userService;

    public ImageRestController(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }

    @GetMapping
    public Image getImage(Authentication authentication) {

        var user = userService.loadUserByUsername(authentication.getName());
        if (user == null) throw new UserNotFoundException();
        return null;
    }

    @GetMapping("/image/{id}")
    public Image getImageById(@PathVariable("id") UUID id) {
        return imageService.getImageByID(id);
    }

    @GetMapping("/{imageName}")
    public ResponseEntity<byte[]> getImageByName(@PathVariable("imageName") String imageName) {
        Image image;
        if (!imageName.contains("."))
            image = imageService.getImageByNameContains(imageName);
        else image = imageService.getImageByName(imageName);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image.data);
    }

    @GetMapping("/id/{id}")
    public byte[] getImage(@PathVariable("id") UUID id) {
        var image = imageService.getImageByID(id);
        return image.data;
    }

    @PostMapping
    public ResponseEntity<String> saveImage(@RequestParam("file") MultipartFile image, RedirectAttributes redirectAttributes, Authentication authentication) throws IOException {
        var savedImage = imageService.uploadImage(image);
        var user = userService.loadUserByUsername(authentication.getName());
        user.setImageUrl(savedImage.getName());
        userService.updateUser(user);
        redirectAttributes.addAttribute("id", savedImage.getId());
//        return "redirect:/auth/images/image/{id}";
        return ResponseEntity.ok("Image uploaded successfully.");
    }

//    @PostMapping("/profile/image")
//    public ResponseEntity<String> uploadImage(@RequestBody byte[] imageBytes, Authentication authentication) {
//        try {
//            var user = userService.loadUserByUsername(authentication.getName());
//            var savedImage = imageService.uploadImage(imageBytes, user.getUsername());
//            user.setImageUrl(savedImage.getName());
//            System.out.println(savedImage.getName());
//            userService.updateUser(user);
//            return ResponseEntity.ok("Image uploaded successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image.");
//        }
//    }

//    @PostMapping("/profile/image")
//    public ResponseEntity<String> uploadImage(@RequestBody InputStream imageStream, Authentication authentication) {
//        try {
//            var user = userService.loadUserByUsername(authentication.getName());
//            var bytes = imageStream.readAllBytes();
//            var savedImage = imageService.uploadImage(bytes, user.getUsername());
//            user.setImageUrl(savedImage.getName());
//            System.out.println(savedImage.getName());
//            userService.updateUser(user);
//            return ResponseEntity.ok("Image uploaded successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image.");
//        }
//    }

    @GetMapping(value = "/i/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable("imageName") String imageName) {
        var image = imageService.getImageByName(imageName);
        byte[] bytes = image.data;
        Resource imageResource = new ByteArrayResource(bytes);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageResource);
    }

}
