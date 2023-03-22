package uge.fr.ugeoverflow.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uge.fr.ugeoverflow.model.Address;
import uge.fr.ugeoverflow.model.Vote;

import java.util.Optional;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

}
