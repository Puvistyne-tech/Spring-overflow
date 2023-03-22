package uge.fr.ugeoverflow;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import uge.fr.ugeoverflow.model.Image;
import uge.fr.ugeoverflow.model.Tag;
import uge.fr.ugeoverflow.model.User;
import uge.fr.ugeoverflow.repository.ImageRepository;
import uge.fr.ugeoverflow.repository.TagRepository;
import uge.fr.ugeoverflow.repository.UserRepository;
import uge.fr.ugeoverflow.security.UserRole;
import uge.fr.ugeoverflow.service.AuthenticationService;
import uge.fr.ugeoverflow.utils.Utils;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class UgeOverflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(UgeOverflowApplication.class, args);
        System.out.println("Hello World");
    }

}
