package com.petpool.domain.model.pet;

import com.petpool.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {


    @Nullable
    @Query(value = "select pet from Pet pet where lower(pet.breed) like lower(concat('%', :breedToFind, '%'))")
    List<Pet> findByBreed(@Param("breedToFind") String breed);

    Optional<Pet> findPetByType(PetType type);

    Optional<Pet> findPetByHost(User user);

    Pet findPetByOwner(User user);

}
