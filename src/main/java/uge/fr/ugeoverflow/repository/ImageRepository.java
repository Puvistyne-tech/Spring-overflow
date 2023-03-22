package uge.fr.ugeoverflow.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uge.fr.ugeoverflow.model.Image;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRepository extends CrudRepository<Image, UUID> {
    Optional<Image> findByName(String name);

    Optional<Image> findFirstByNameContains(String name);
}
