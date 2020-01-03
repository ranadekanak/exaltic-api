package org.exaltic.app.controller;

import java.util.Optional;

import org.exaltic.app.domain.Attribute;
import org.exaltic.app.domain.Trainer;
import org.exaltic.app.domain.User;
import org.exaltic.app.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pri")
public class TrainerController {

	final private TrainerRepository trainerRepository;
	
	@Autowired
	public TrainerController(TrainerRepository trainerRepository) {
		this.trainerRepository = trainerRepository;
	}
	
	@PutMapping("/trainer/{trainerId}")
	public ResponseEntity updateTrainerAttributes(@PathVariable(name = "trainerId") Long trainerId, @RequestBody Trainer trainerDTO) {
		Optional<User> user = trainerRepository.findById(trainerId);
		if(user.isPresent() && user.get().isEnabled()) {
			Trainer trainer = (Trainer) user.get();
			trainer.setVersion(trainer.getVersion()+1);
			trainer.setTitle(trainerDTO.getTitle());
			trainer.setProfile(trainerDTO.getProfile());
			trainer.setIsPremium(trainerDTO.getIsPremium());
			
			if(trainerDTO.getAttributes() != null) {
				trainerDTO.getAttributes().forEach(e -> {
					Attribute attribute = new Attribute();
					attribute.setEnabled(Boolean.TRUE);
					attribute.setVersion(1);
					attribute.setName(e.getName());
					attribute.setValue(e.getValue());
					attribute.setUnit(e.getUnit());
					attribute.setTrainer(trainer);

					trainer.getAttributes().add(attribute);
				});
			}
			return ResponseEntity.ok(trainerRepository.save(trainer));
		}
		return ResponseEntity.badRequest().build();
		
	}
	
}
