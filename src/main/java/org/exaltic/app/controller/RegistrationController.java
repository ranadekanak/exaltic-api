package org.exaltic.app.controller;

import org.exaltic.app.domain.Device;
import org.exaltic.app.domain.Trainer;
import org.exaltic.app.domain.User;
import org.exaltic.app.dto.RegistrationDTO;
import org.exaltic.app.enums.DeviceType;
import org.exaltic.app.repository.UserRepository;
import org.exaltic.app.service.AmazonS3ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pub")
public class RegistrationController {

	private static final String SUFFIX = "/";
	private static final String PORTFOLIO_KEY = "portfolio";
	
	final private UserRepository<User> userRepository;
	final private UserRepository<Trainer> trainerRepository;
	final private AmazonS3ClientService amazonS3ClientService;

	@Autowired
	public RegistrationController(UserRepository<User> userRepository, UserRepository<Trainer> trainerRepository, AmazonS3ClientService amazonS3ClientService) {
		this.userRepository = userRepository;
		this.trainerRepository = trainerRepository;
		this.amazonS3ClientService = amazonS3ClientService;
	}

	@PostMapping("/register")
	public ResponseEntity registerUser(@RequestBody RegistrationDTO registrationDTO) {
		if (userRepository.existsByEmail(registrationDTO.getEmail())) {
			return ResponseEntity.badRequest().build();
		}
		User user = new User();
		user.setEnabled(Boolean.TRUE);
		user.setVersion(1);
		user.setFirstName(registrationDTO.getFirstName());
		user.setLastName(registrationDTO.getLastName());
		user.setEmail(registrationDTO.getEmail());
		user.setProvider(registrationDTO.getProvider());
		user.setImageUrl(registrationDTO.getImageUrl());

		Device device = new Device();
		device.setEnabled(Boolean.TRUE);
		device.setVersion(1);
		device.setDeviceId(registrationDTO.getDeviceId());
		device.setDeviceType(DeviceType.valueOf(registrationDTO.getDeviceType()));
		device.setUser(user);

		if (!user.getDevices().contains(device)) {
			user.getDevices().add(device);
		}

		return ResponseEntity.ok(userRepository.save(user));
	}

	@PostMapping("/register/trainer")
	public ResponseEntity registerTrainer(@RequestBody RegistrationDTO registrationDTO) {
		if (trainerRepository.existsByEmail(registrationDTO.getEmail())) {
			return ResponseEntity.badRequest().build();
		}
		Trainer trainer = new Trainer();
		trainer.setEnabled(Boolean.TRUE);
		trainer.setVersion(1);
		trainer.setFirstName(registrationDTO.getFirstName());
		trainer.setLastName(registrationDTO.getLastName());
		trainer.setEmail(registrationDTO.getEmail());
		trainer.setProvider(registrationDTO.getProvider());
		trainer.setImageUrl(registrationDTO.getImageUrl());

		Device device = new Device();
		device.setEnabled(Boolean.TRUE);
		device.setVersion(1);
		device.setDeviceId(registrationDTO.getDeviceId());
		device.setDeviceType(DeviceType.valueOf(registrationDTO.getDeviceType()));
		device.setUser(trainer);

		if (!trainer.getDevices().contains(device)) {
			trainer.getDevices().add(device);
		}

		trainer = trainerRepository.save(trainer);
		amazonS3ClientService.createBucket(trainer.getId().toString());
		amazonS3ClientService.createBucket(trainer.getId().toString()+SUFFIX+PORTFOLIO_KEY);
		return ResponseEntity.ok(trainer);
	}

}
