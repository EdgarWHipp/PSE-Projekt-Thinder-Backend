package com.pse.thinder.backend.services;

import java.io.IOException;
import java.util.*;

import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotFoundException;
import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.InputValidation;
import com.pse.thinder.backend.databaseFeatures.Pair;
import com.pse.thinder.backend.databaseFeatures.ThesisDTO;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;
import com.pse.thinder.backend.databaseFeatures.thesis.Image;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesesForDegree;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRating;
import com.pse.thinder.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Validator;

@Service
public class ThesisService {

	private static final String THESIS_NOT_FOUND = "Thesis not found";
	@Autowired
	private ThesisRepository thesisRepository;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private DegreeRepository degreeRepository;

	@Autowired
	private ThesesForDegreeRepository thesesForDegreeRepository;

	@Autowired
	private Validator validator;


	@Transactional
	public ThesisDTO getThesisById(UUID id) {
		Thesis thesis = thesisRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(THESIS_NOT_FOUND));
		return parseToDto(Arrays.asList(thesis)).get(0); //id is the primary key thus, the list has only one element.
	}

	//todo user eingaben müssen besser überprüft werden, bsp. degree dürfen nicht leer
	@Transactional
	public void addThesis(ThesisDTO thesis, Supervisor supervisor) {
		List<Degree> degreeList = degreeRepository.findAllById(thesis.getPossibleDegrees().stream()
				.map(degree -> degree.getId()).toList());
		//todo check if supervisor and degree are at the same university.
		Thesis newThesis = new Thesis(thesis.getName(), thesis.getTask(), thesis.getMotivation(), thesis.getQuestions()
				,thesis.getSupervisingProfessor(), supervisor);
		List<ThesesForDegree> possibleDegrees = new ArrayList<>();
		for(Degree degree : degreeList){
			ThesesForDegree thesesForDegree = new ThesesForDegree(degree, newThesis);
			degree.addPossibleThesis(thesesForDegree);
			possibleDegrees.add(thesesForDegree);
		}
		newThesis.setPossibleDegrees(possibleDegrees);
		thesisRepository.saveAndFlush(newThesis);
		thesesForDegreeRepository.saveAllAndFlush(possibleDegrees);

		List<Image> images = new ArrayList<>();

		for(String encodedImage : thesis.getImages()){
			byte[] image = java.util.Base64.getDecoder().decode(encodedImage);
			images.add(new Image(image, newThesis));
			}

		if(images.size() != 0) {
			imageRepository.saveAllAndFlush(images);
		}
	}

	public Pair<Integer, Integer> getThesisRatings(UUID thesisId){
		Thesis thesis = getActualThesisById(thesisId);
		ArrayList<ThesisRating> thesisRatings = thesis.getStudentRatings();
		int positiveRated = 0;
		int negativeRated = 0;
		for (ThesisRating rating : thesisRatings){
			if(rating.getPositiveRated()){
				positiveRated++;
			} else {
				negativeRated++;
			}
		}
		return new Pair(Integer.valueOf(positiveRated), Integer.valueOf(negativeRated));

	}

	@Transactional
	public void updateThesis(ThesisDTO newThesis, UUID thesisId)  {
		Thesis oldThesis = getActualThesisById(thesisId);
		if(thesisId != newThesis.getId()){
			//todo add exception
		}

		System.err.println("TEST1");
		if(validator.validateProperty(newThesis, "name", InputValidation.class).isEmpty()){
			oldThesis.setName(newThesis.getName());
		}
		System.err.println("TEST2");
		if(validator.validateProperty(newThesis, "task", InputValidation.class).isEmpty()){
			oldThesis.setTask(newThesis.getTask());
		}
		System.err.println("TEST3");
		if(validator.validateProperty(newThesis, "motivation", InputValidation.class).isEmpty()){
			oldThesis.setMotivation(newThesis.getMotivation());
		}
		System.err.println("TEST4");
		if(validator.validateProperty(newThesis, "questions", InputValidation.class).isEmpty()){
			oldThesis.setQuestionForm(newThesis.getQuestions());
		}
		System.err.println("TEST5");
		if(validator.validateProperty(newThesis, "supervisingProfessor", InputValidation.class).isEmpty()){
			oldThesis.setSupervisingProfessor(newThesis.getSupervisingProfessor());
		}
		System.err.println("TEST6");
		List<Image> decodedImages = new ArrayList<>();
		if(validator.validateProperty(newThesis, "images", InputValidation.class).isEmpty()){
			imageRepository.deleteAllById(oldThesis.getImages().stream().map(image -> image.getId()).toList());
			//oldThesis.getImages().removeAll(oldThesis.getImages());
			//does this workaround work?
			List<String> encodedImages = newThesis.getImages();
			for(String encodedImage : encodedImages){
				byte[] decodedImage = Base64.getDecoder().decode(encodedImage);
				decodedImages.add(new Image(decodedImage, oldThesis));
			}
			oldThesis.setImages(decodedImages);
		}
		System.err.println("TEST7" + newThesis.getPossibleDegrees().size());
		//System.err.println(newThesis.getPossibleDegrees().get(0));
		List<ThesesForDegree> possibleDegreess = new ArrayList<>();
		/*try{
			if(validator.validateProperty(newThesis, "possibleDegrees", InputValidation.class).isEmpty()){
			System.err.println("TEST7" + newThesis.getPossibleDegrees().size());
			for(Degree possibleDegree : newThesis.getPossibleDegrees()){
				possibleDegreess.add(new ThesesForDegree(possibleDegree, oldThesis));
			}
			oldThesis.setPossibleDegrees(possibleDegreess);
			}
		} catch (Exception e){
			System.err.println(e.getMessage());
		} */
		System.err.println("TEST1");
		thesisRepository.saveAndFlush(oldThesis);
		imageRepository.saveAllAndFlush(decodedImages);
		thesesForDegreeRepository.saveAllAndFlush(possibleDegreess);
	}

	@Transactional //transactional because session is needed
	public List<ThesisDTO> getThesesBySupervisor(UUID id){
		return parseToDto(thesisRepository.findBySupervisorId(id));
	}

	public void addImages(ArrayList<MultipartFile> imageFiles, UUID thesisId) throws IOException {
		Thesis thesis = getActualThesisById(thesisId);
		ArrayList<Image> newImages = new ArrayList<>();
		for(MultipartFile imageFile : imageFiles){
			if(imageFile.isEmpty()){
				//todo add exception
			}
			Image newImage = new Image();
			newImage.setImage(imageFile.getBytes());
			newImage.setThesis(thesis);
			newImages.add(newImage);
		}
		thesis.setImages(newImages);
		thesisRepository.save(thesis);

		imageRepository.saveAll(newImages);
	}

	public void removeImages(UUID thesisId){
		Thesis thesis = getActualThesisById(thesisId);
		thesis.getImages().clear();
		thesisRepository.save(thesis);
	}
	
	public void deleteThesis(UUID id) {
		thesisRepository.deleteById(id);
	}

	@Transactional
	public List<ThesisDTO> parseToDto(List<Thesis> theses){
		return theses.stream().map(thesis -> new ThesisDTO(
				thesis.getId(),
				thesis.getName(),
				thesis.getSupervisingProfessor(),
				thesis.getMotivation(),
				thesis.getTask(),
				thesis.getQuestionForm(),
				thesis.getSupervisor(),
				thesis.getEncodedImages(),
				thesis.getPossibleDegrees().stream().map(thesesForDegree -> thesesForDegree.getDegree()).toList()
		)).toList();
	}


	public Thesis getActualThesisById(UUID thesisId){
		return thesisRepository.findById(thesisId).orElseThrow(() -> new EntityNotFoundException(THESIS_NOT_FOUND));
	}
}
