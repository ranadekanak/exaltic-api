package org.exaltic.app.controller;

import org.exaltic.app.domain.User;
import org.exaltic.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pri")
public class RegistrationController {

	final private UserRepository<User> userRepository;
	
	@Autowired
	public RegistrationController(UserRepository<User> userRepository) {
		this.userRepository = userRepository;
	}
	
	@PutMapping("/register")
	public ResponseEntity registerUser(@RequestBody User user) {
		return ResponseEntity.ok(userRepository.save(user));
		
	}
	
}
