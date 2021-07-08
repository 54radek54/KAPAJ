package com.shelter.shelter.controller;

import com.shelter.shelter.model.User;
import com.shelter.shelter.repository.UserRepository;
import com.shelter.shelter.service.UserServices;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

@Controller
public class AppController {


	private final UserServices service;

	private final UserRepository userRepository;

	public AppController(UserServices service, UserRepository userRepository) {
		this.service = service;
		this.userRepository = userRepository;
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		return "signup_form";
	}
	
	@PostMapping("/process_register")
	public String processRegister(User user, HttpServletRequest request) 
			throws UnsupportedEncodingException, MessagingException {
		service.register(user, getSiteURL(request));		
		return "register_success";
	}

	@GetMapping("/users")
	public String listUsers(Model model) {
		List<User> listUsers = service.listAll();
		model.addAttribute("listUsers", listUsers);
		return "users";
	}
	
	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}	
	
	@GetMapping("/verify")
	public String verifyUser(@Param("code") String code) {
		if (service.verify(code)) {
			return "verify_success";
		} else {
			return "verify_fail";
		}
	}

	@GetMapping("/deleteUser/{id}")
	public String deleteUser(@PathVariable("id") Long id){
		service.deleteUserById(id);
		return "redirect:/users";
	}

	@GetMapping("/edit")
	public String showUpdateForm( Model model){
		model.addAttribute("user", new User());
		return "updateUser";
	}

	@PostMapping("/update")
	public String updateUser(User user, Principal principal) {
		User updateuser = userRepository.findByEmail(principal.getName());
		updateuser.setFirstName(user.getFirstName());
		updateuser.setLastName(user.getLastName());
		userRepository.save(updateuser);
		return "redirect:/schronisko";
	}

}
