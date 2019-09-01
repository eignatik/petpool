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
    @Query("select pet from Pet pet where lower(pet.breed) = lower(:breed)")
    List<Pet> findByBreed(@Param("breed") String breed);
    Optional<Pet> findPetByType(PetType type);
    Optional<Pet> findPetByUser(User user);

}
