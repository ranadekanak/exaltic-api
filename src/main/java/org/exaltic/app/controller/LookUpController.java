package org.exaltic.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.exaltic.app.domain.Category;
import org.exaltic.app.domain.Trainer;
import org.exaltic.app.domain.User;
import org.exaltic.app.repository.CategoryRepository;
import org.exaltic.app.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pub")
public class LookUpController {

	final private CategoryRepository categoryRepository;
	final private TrainerRepository trainerRepository;
	
	@Autowired
	public LookUpController(CategoryRepository categoryRepository, TrainerRepository trainerRepository) {
		this.categoryRepository = categoryRepository;
		this.trainerRepository = trainerRepository;
	}
	
	@GetMapping("/categories")
    public List<Category> getCategories(){
        return categoryRepository.findAll().stream().filter(e -> e.isEnabled()).collect(Collectors.toList());
    }
	
	@GetMapping("/trainers")
    public List<User> getTrainers(){
        return trainerRepository.findAll().stream().filter(e -> e.isEnabled() && e instanceof Trainer).collect(Collectors.toList());
    }
}
