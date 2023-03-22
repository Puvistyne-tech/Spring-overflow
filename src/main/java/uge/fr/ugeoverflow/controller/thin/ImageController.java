package uge.fr.ugeoverflow.controller.thin;

import org.springframework.stereotype.Controller;
import uge.fr.ugeoverflow.service.ImageService;
import uge.fr.ugeoverflow.service.UserService;

@Controller
//@RequestMapping({"/auth/images", "/images"})
public class ImageController {

    private final ImageService imageService;
    private final UserService userService;

    public ImageController(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }

//    @GetMapping("/{id}")
//    public byte[] getImage(@PathVariable UUID id) {
//        Image image = imageService.getImageByID(id);
//        return image.getData();
//    }
//
//    @GetMapping
//    public String displayImageForm() {
//        return "image";
//    }


//    @PostMapping
//    //@PreAuthorizeAdminAndAuthUser
//    public String saveImage(
//            @RequestParam("file") MultipartFile image,
//            @ModelAttribute("question") OneQuestionDTO question,
//            RedirectAttributes redirectAttributes,
//            Authentication authentication,
//            Model model
//    ) throws IOException {
//        var savedImage = imageService.uploadImage(image);
//        User user = (User) authentication.getPrincipal();
//        user = userService.loadUserByUsername(user.getUsername());
//        user.setImageUrl(savedImage.getImageUrl());
//        userService.updateUser(user);
//        redirectAttributes.addAttribute("id", savedImage.getId());
////        return "redirect:/image/{id}";
//        model.addAttribute("question", question);
////        return "redirect:/questions/ask";
//        return "ask-question";
//    }

}
