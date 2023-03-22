package uge.fr.ugeoverflow.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uge.fr.ugeoverflow.model.Address;
import uge.fr.ugeoverflow.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    //unique username
    boolean existsByUsername(String username);

    boolean existsUserById(UUID id);


    boolean existsByEmail(String email);

    boolean existsByFirstname(String firstname);

    boolean existsByLastname(String lastname);

    boolean existsByToken(String token);


//    List<User> findByFollowers(User user);

}
