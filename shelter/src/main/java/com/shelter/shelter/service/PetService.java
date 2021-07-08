package com.shelter.shelter.service;

import com.shelter.shelter.model.Pet;
import com.shelter.shelter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public List<Pet> getAllPets(){return petRepository.findAll();}

    public List<Pet> shelterPets(){return petRepository.findAll();}

    public void deletePetById(Long id){petRepository.deleteById(id);}

    public void getPetById(Long id){petRepository.findById(id);}

}
