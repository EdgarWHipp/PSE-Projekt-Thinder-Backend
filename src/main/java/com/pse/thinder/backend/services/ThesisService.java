package com.pse.thinder.backend.services;

import java.util.*;
import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotFoundException;
import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.InputValidation;
import com.pse.thinder.backend.databaseFeatures.dto.ThesisDTO;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;
import com.pse.thinder.backend.databaseFeatures.thesis.Image;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesesForDegree;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import com.pse.thinder.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Validator;

@Service
public class ThesisService {

	private static final String THESIS_NOT_FOUND = "Thesis not found";

	private static final String WRONG_DEGREE = "You are only allowed to add theses to degrees which are registered at " +
			"your own university!";

	private static final String  WRONG_ID = "You are not allowed to change the id of theses!";

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

	@Transactional
	public void addThesis(ThesisDTO thesis, Supervisor supervisor) {
		List<Degree> degreeList = degreeRepository.findAllById(thesis.getPossibleDegrees().stream()
				.map(degree -> degree.getId()).toList());

		Thesis newThesis = new Thesis(thesis.getName(), thesis.getTask(), thesis.getMotivation(), thesis.getQuestions()
				,thesis.getSupervisingProfessor(), supervisor);
		List<ThesesForDegree> possibleDegrees = new ArrayList<>();
		UUID supervisorUniversityId = supervisor.getUniversity().getId();
		for(Degree degree : degreeList){
			if (degree.getUniversity().getId().compareTo(supervisorUniversityId) != 0) {
				throw new IllegalArgumentException(WRONG_DEGREE);
			}
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

	@Transactional
	public void updateThesis(ThesisDTO newThesis, UUID thesisId)  {
		Thesis oldThesis = getActualThesisById(thesisId);
		if(thesisId != newThesis.getId()){
			throw new IllegalArgumentException(WRONG_ID);
		}

		if(validator.validateProperty(newThesis, "name", InputValidation.class).isEmpty()){
			oldThesis.setName(newThesis.getName());
		}
		if(validator.validateProperty(newThesis, "task", InputValidation.class).isEmpty()){
			oldThesis.setTask(newThesis.getTask());
		}
		if(validator.validateProperty(newThesis, "motivation", InputValidation.class).isEmpty()){
			oldThesis.setMotivation(newThesis.getMotivation());
		}
		if(validator.validateProperty(newThesis, "questions", InputValidation.class).isEmpty()){
			oldThesis.setQuestionForm(newThesis.getQuestions());
		}
		if(validator.validateProperty(newThesis, "supervisingProfessor", InputValidation.class).isEmpty()){
			oldThesis.setSupervisingProfessor(newThesis.getSupervisingProfessor());
		}
		List<Image> decodedImages = new ArrayList<>();
		if(validator.validateProperty(newThesis, "images", InputValidation.class).isEmpty()){
			imageRepository.deleteAllByThesisId(oldThesis.getId());
			List<String> encodedImages = newThesis.getImages();
			for(String encodedImage : encodedImages){
				byte[] decodedImage = Base64.getDecoder().decode(encodedImage);
				decodedImages.add(new Image(decodedImage, oldThesis));
			}
			oldThesis.setImages(decodedImages);
			imageRepository.saveAllAndFlush(decodedImages);
		}
		List<ThesesForDegree> possibleDegrees = new ArrayList<>();
		if(validator.validateProperty(newThesis, "possibleDegrees", InputValidation.class).isEmpty()){
			thesesForDegreeRepository.deleteAllByThesisId(oldThesis.getId());
			for(Degree possibleDegree : newThesis.getPossibleDegrees()){
				possibleDegrees.add(new ThesesForDegree(possibleDegree, oldThesis));
			}
			thesesForDegreeRepository.saveAllAndFlush(possibleDegrees);
			oldThesis.setPossibleDegrees(possibleDegrees);
			}
		thesisRepository.saveAndFlush(oldThesis);

	}

	@Transactional
	public List<ThesisDTO> getThesesBySupervisor(UUID id){
		return parseToDto(thesisRepository.findBySupervisorId(id));
	}


	public void deleteThesis(UUID id) {
		thesisRepository.deleteById(id);
	}

	public Thesis getActualThesisById(UUID thesisId){
		return thesisRepository.findById(thesisId).orElseThrow(() -> new EntityNotFoundException(THESIS_NOT_FOUND));
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
				thesis.getNumPositiveRated(),
				thesis.getNumNegativeRated(),
				thesis.getSupervisor(),
				thesis.getEncodedImages(),
				thesis.getPossibleDegrees().stream().map(thesesForDegree -> thesesForDegree.getDegree()).toList()
		)).toList();
	}

}
