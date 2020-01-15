package org.exaltic.app.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.exaltic.app.domain.Attribute;
import org.exaltic.app.domain.Category;
import org.exaltic.app.domain.Certificate;
import org.exaltic.app.domain.Media;
import org.exaltic.app.domain.Trainer;
import org.exaltic.app.domain.User;
import org.exaltic.app.dto.CertificateDTO;
import org.exaltic.app.dto.MultiMediaUploadRequest;
import org.exaltic.app.repository.CategoryRepository;
import org.exaltic.app.repository.TrainerRepository;
import org.exaltic.aws.service.AmazonS3ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

@RestController
@RequestMapping("/api/pri")
public class TrainerController {

	private static final String APP_BUCKET_NAME = "exaltic-bucket";
	private static final String SUFFIX = "/";
	private static final String PROFILE_PHOTO_KEY = "profile_photo";
	private static final String PORTFOLIO_MEDIA_KEY = "portfolio_media";
	
	final private TrainerRepository trainerRepository;
	final private CategoryRepository categoryRepository;
	final private AmazonS3ClientService amazonS3ClientService;
	
	@Autowired
	public TrainerController(TrainerRepository trainerRepository, CategoryRepository categoryRepository, AmazonS3ClientService amazonS3ClientService) {
		this.trainerRepository = trainerRepository;
		this.categoryRepository = categoryRepository;
		this.amazonS3ClientService = amazonS3ClientService;
	}
	
	@GetMapping("/trainer/{trainerId}/profilePhoto")
	public void getProfilePhoto(@PathVariable(name = "trainerId") Long trainerId, HttpServletResponse response) {
		Optional<User> user = trainerRepository.findById(trainerId);
		if(user.isPresent() && user.get().isEnabled()) {
			Trainer trainer = (Trainer) user.get();
			S3ObjectInputStream s3ObjectInputStream = amazonS3ClientService.downloadFileFromBucket(APP_BUCKET_NAME, trainer.getId().toString()+SUFFIX+PROFILE_PHOTO_KEY);
	        try {
				IOUtils.copy(s3ObjectInputStream, response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@PutMapping("/trainer/{trainerId}/profilePhoto")
	public ResponseEntity uploadProfilePhoto(@PathVariable(name = "trainerId") Long trainerId, @RequestParam("file") MultipartFile multipartFile) {
		Optional<User> user = trainerRepository.findById(trainerId);
		if(user.isPresent() && user.get().isEnabled()) {
			Trainer trainer = (Trainer) user.get();
			try {
				ObjectMetadata metaData = amazonS3ClientService.uploadFileToBucket(APP_BUCKET_NAME, trainer.getId().toString()+SUFFIX+PROFILE_PHOTO_KEY, multipartFile.getInputStream());
				String path = "/trainer"+SUFFIX+trainer.getId()+"/profilePhoto";
				trainer.setImageUrl(path);
				return ResponseEntity.ok(trainerRepository.save(trainer));
			} catch (IOException e) {
				return ResponseEntity.badRequest().build();
			}
		}
		return ResponseEntity.badRequest().build();
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
	
	@PutMapping("/trainer/{trainerId}/media")
	public ResponseEntity uploadMedia(@PathVariable(name = "trainerId") Long trainerId, @RequestBody MultiMediaUploadRequest mediaUploadRequest) {
		Optional<User> user = trainerRepository.findById(trainerId);
		if(user.isPresent()) {
			Trainer trainer = (Trainer) user.get();
			try {
				Media media = new Media();
				media.setEnabled(Boolean.TRUE);
				media.setVersion(1);
				
				media.setName(mediaUploadRequest.getName());
				media.setDescription(mediaUploadRequest.getDescription());
				media.setType(mediaUploadRequest.getType());
				
				amazonS3ClientService.uploadFileToBucket(APP_BUCKET_NAME, trainer.getId().toString()+"/"+PORTFOLIO_MEDIA_KEY, mediaUploadRequest.getFile().getInputStream());
				media.setPath("/trainer/"+trainer.getId()+"/media/"+media.getId());
				media.setTrainer(trainer);
				
				trainer.getMedia().add(media);
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
			return ResponseEntity.ok(trainerRepository.save(trainer));
		}
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/trainer/{trainerId}/media/{mediaId}")
	public void getMedia(@PathVariable(name = "trainerId") Long trainerId, @PathVariable(name = "mediaId") Long mediaId, HttpServletResponse response) {
		Optional<User> user = trainerRepository.findById(trainerId);
		if(user.isPresent() && user.get().isEnabled()) {
			Trainer trainer = (Trainer) user.get();
			S3ObjectInputStream s3ObjectInputStream = amazonS3ClientService.downloadFileFromBucket(APP_BUCKET_NAME, trainer.getId().toString()+SUFFIX+PORTFOLIO_MEDIA_KEY+SUFFIX+mediaId);
	        try {
				IOUtils.copy(s3ObjectInputStream, response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
