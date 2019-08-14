package com.petpool.domain.model.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    Optional<Pet> findPetByBreed(String breed);

    Optional<Pet> findPetByName(String petName);

    Optional<Pet> findPetByType(PetType type);

}
