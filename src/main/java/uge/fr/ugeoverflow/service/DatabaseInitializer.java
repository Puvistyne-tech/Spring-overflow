//package uge.fr.ugeoverflow.service;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import uge.fr.ugeoverflow.model.Address;
//import uge.fr.ugeoverflow.model.User;
//import uge.fr.ugeoverflow.repository.UserRepository;
//import uge.fr.ugeoverflow.security.UserRole;
//
//import java.time.LocalDateTime;
//
//@Component
//public class DatabaseInitializer implements CommandLineRunner {
//
//    private final TagService tagService;
//
//    private final UserRepository userRepository;
//    private final AuthenticationService authenticationService;
//    private final PasswordEncoder passwordEncoder;
//
//    public DatabaseInitializer(TagService tagService, UserRepository userRepository, AuthenticationService authenticationService, PasswordEncoder passwordEncoder) {
//        this.tagService = tagService;
//        this.userRepository = userRepository;
//        this.authenticationService = authenticationService;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    public void createAdmin() {
//        if (userRepository.findByUsername("admin").isEmpty()) {
//            var admin = new User("admin", "admin", "admin", "admin@admin.com", passwordEncoder.encode("admin"));
//            admin.setRole(UserRole.ADMIN.name());
//            admin.setToken(authenticationService.generateToken(admin));
//            admin.setCreationTime(LocalDateTime.now());
//            admin.setAddress(new Address("street", "city", "France", "12434"));
//            userRepository.save(admin);
//        }
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        tagService.initialize();
//        createAdmin();
//    }
//}
