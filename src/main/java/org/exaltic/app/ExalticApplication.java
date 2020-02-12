package org.exaltic.app;

import javax.annotation.PostConstruct;

import org.exaltic.app.domain.Category;
import org.exaltic.app.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ExalticApplication {

	@Autowired
	private CategoryRepository categoryRepository;
	
    public static void main(String[] args) {
        SpringApplication.run(ExalticApplication.class, args);
    }
    
    @PostConstruct
	public void init() {
		if (categoryRepository.findAll().isEmpty()) {
			categoryRepository.save(new Category("Gym"));
			categoryRepository.save(new Category("Swimming"));
			categoryRepository.save(new Category("Football"));
			categoryRepository.save(new Category("Cycling"));
			categoryRepository.save(new Category("Cricket"));
			categoryRepository.save(new Category("Marathon"));
			categoryRepository.save(new Category("Karate"));
			categoryRepository.save(new Category("Tennis"));
			categoryRepository.save(new Category("Boxing"));
			categoryRepository.save(new Category("Badminton"));
		}
	}
    
}
