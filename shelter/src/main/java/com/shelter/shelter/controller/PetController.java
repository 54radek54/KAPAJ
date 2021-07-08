package com.shelter.shelter.controller;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.shelter.shelter.exception.AdoptionException;
import com.shelter.shelter.model.Pet;
import com.shelter.shelter.model.User;
import com.shelter.shelter.repository.PetRepository;
import com.shelter.shelter.repository.UserRepository;
import com.shelter.shelter.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.security.Principal;

@Controller
public class PetController {

    private final PetRepository petRepository;

    @Autowired
    ServletContext servletContext;

    private final TemplateEngine templateEngine;

    public PetController(PetRepository petRepository, TemplateEngine templateEngine) {
        this.petRepository = petRepository;
        this.templateEngine = templateEngine;
    }

    @Autowired
    private PetService petService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/deletePet/{id}")
    public String deletePet(@PathVariable("id") Long id){
        petService.deletePetById(id);
        return "redirect:/pets";
    }

    @GetMapping("/pets")
    public String getAllPets(Model model) {
        model.addAttribute("newPet", new Pet());
        model.addAttribute("pets", petRepository.findAll());
        return "employee";
    }

    @PostMapping("/add-pet")
    public String addPet(@ModelAttribute Pet pet) {
        petRepository.save(pet);
        return "redirect:/pets";
    }

    @GetMapping("/schronisko")
    public String shelterPets(Model model) {
        model.addAttribute("nPet", new Pet());
        model.addAttribute("shelterPets", petRepository.findAll());
        return "shelter";
    }

    @GetMapping("/{petId}/user")
    public String assignPetToUser(@PathVariable Long petId, Principal principal) throws AdoptionException {
        Pet pet = petRepository.findById(petId).get();
        if(pet.getUser()!=null){
           throw new AdoptionException("To zwierze zostalo juz adoptowane");
        }
        User user = userRepository.findByEmail(principal.getName());
        pet.setUser(user);
        petRepository.save(pet);
        return "redirect:/schronisko";
    }

    @GetMapping("/{petId}/user/delete")
    public String deleteAssignPetToUser(@PathVariable Long petId) throws AdoptionException {
        Pet pet = petRepository.findById(petId).get();
        if(pet.getUser()==null && !pet.isStan()){
            throw new AdoptionException("To zwierze nie zostalo jeszcze adoptowane");
        }
        pet.setUser(null);
        pet.setStan(false);
        petRepository.save(pet);
        return "redirect:/pets";
    }

    @GetMapping("/viewpet/{petId}")
    public String viewPet(@PathVariable Long petId, Model model){
        model.addAttribute("newPet", new Pet());
        model.addAttribute("shelterPet", petRepository.findById(petId));
        return "view";
    }

    @GetMapping("/adoptionState/{petId}")
    public String adoptionState(@PathVariable Long petId) throws AdoptionException{
        Pet pet = petRepository.findById(petId).get();
        if(pet.getUser()==null){
            throw new AdoptionException("To zwierze nie zostalo jeszcze adoptowane");
        }
        pet.setStan(true);
        petRepository.save(pet);
        return "redirect:/pets";
    }

    @RequestMapping(path = "/pdf/{petId}")
    public ResponseEntity<?> getPDF(@PathVariable Long petId,HttpServletRequest request, HttpServletResponse response) {

        Pet pet = petRepository.findById(petId).get();

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("sheltersPet", pet);
        String orderHtml = templateEngine.process("pdf", context);

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");

        HtmlConverter.convertToPdf(orderHtml, target, converterProperties);

        byte[] bytes = target.toByteArray();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=YourPet.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }

}
