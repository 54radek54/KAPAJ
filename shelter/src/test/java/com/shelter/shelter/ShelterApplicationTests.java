package com.shelter.shelter;

import com.shelter.shelter.model.User;
import com.shelter.shelter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class ShelterApplicationTests {

	@Autowired
	private UserRepository repo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	public void saveUser() {
		User user = new User();
		user.setRole("ROLE_USER");
		user.setEmail("user@gmail.com");
		user.setPassword(passwordEncoder.encode("userpassword"));
		user.setLastName("userlast");
		user.setFirstName("userfirst");
		user.setEnabled(true);
		repo.save(user);
	}

	@Test
	public void saveAdmin() {
		User admin = new User();
		admin.setRole("ROLE_ADMIN");
		admin.setEmail("admin@gmail.com");
		admin.setPassword(passwordEncoder.encode("adminpassword"));
		admin.setLastName("adminlast");
		admin.setFirstName("adminfirst");
		admin.setEnabled(true);
		repo.save(admin);
	}

	@Test
	public void saveEmployee() {
		User employee = new User();
		employee.setRole("ROLE_EMPLOYEE");
		employee.setEmail("employee@gmail.com");
		employee.setPassword(passwordEncoder.encode("employeepassword"));
		employee.setLastName("employeelast");
		employee.setFirstName("employeefirst");
		employee.setEnabled(true);
		repo.save(employee);
	}
}
