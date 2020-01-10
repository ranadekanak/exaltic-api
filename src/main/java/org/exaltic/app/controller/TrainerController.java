package org.exaltic.app.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.exaltic.app.domain.Attribute;
import org.exaltic.app.domain.Category;
import org.exaltic.app.domain.Certificate;
import org.exaltic.app.domain.Trainer;
import org.exaltic.app.domain.User;
import org.exaltic.app.dto.CertificateDTO;
import org.exaltic.app.repository.CategoryRepository;
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
	final private CategoryRepository categoryRepository;
	
	@Autowired
	public TrainerController(TrainerRepository trainerRepository, CategoryRepository categoryRepository) {
		this.trainerRepository = trainerRepository;
		this.categoryRepository = categoryRepository;
	}
	
	@PutMapping("/trainer/{trainerId}/attributes")
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
	
	@PutMapping("/trainer/{trainerId}/categories")
	public ResponseEntity updateTrainerCategoryPreference(@PathVariable(name = "trainerId") Long trainerId, @RequestBody Collection<Long> categoryIds) {
		Optional<User> user = trainerRepository.findById(trainerId);
		if(user.isPresent()) {
			List<Category> categories = categoryRepository.findAll().stream().filter(e -> categoryIds.contains(e.getId())).collect(Collectors.toList());
			Trainer trainer = (Trainer) user.get();
			trainer.setCategories(categories);
			return ResponseEntity.ok(trainerRepository.save(trainer));
		}
		return ResponseEntity.badRequest().build();
	}
	
	@PutMapping("/trainer/{trainerId}/certificates")
	public ResponseEntity updateTrainerCertificates(@PathVariable(name = "trainerId") Long trainerId, @RequestBody Collection<CertificateDTO> certificateList) {
		Optional<User> user = trainerRepository.findById(trainerId);
		if(user.isPresent()) {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Trainer trainer = (Trainer) user.get();
			certificateList.forEach(e -> {
				Certificate cert = new Certificate();
				cert.setEnabled(Boolean.TRUE);
				cert.setVersion(1);
				cert.setTitle(e.getTitle());
				cert.setIssuer(e.getIssuer());
				try {
					if(e.getIssuedDate() != null) {
							cert.setIssuedDate(format.parse(e.getIssuedDate()));
					}
					if(e.getExpiryDate() != null) {
						cert.setExpiryDate(format.parse(e.getExpiryDate()));
					}
				} catch (ParseException exception) {
					exception.printStackTrace();
				}
				cert.setTrainer(trainer);
				trainer.getCertificates().add(cert);
			});
			
			return ResponseEntity.ok(trainerRepository.save(trainer));
		}
		return ResponseEntity.badRequest().build();
	}
	
}
