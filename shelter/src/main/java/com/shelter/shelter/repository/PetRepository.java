package com.shelter.shelter.repository;

import com.shelter.shelter.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findAllByName(String name);
}
