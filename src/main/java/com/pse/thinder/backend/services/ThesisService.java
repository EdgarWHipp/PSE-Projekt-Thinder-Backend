package com.pse.thinder.backend.services;

import java.io.IOException;
import java.util.*;

import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotFoundException;
import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.ThesisDTO;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;
import com.pse.thinder.backend.databaseFeatures.thesis.Image;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesesForDegree;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import com.pse.thinder.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.springframework.web.multipart.MultipartFile;

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


	
	public Thesis getThesisById(UUID id) {
		return thesisRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(THESIS_NOT_FOUND));
	}

	//todo user eingaben müssen besser überprüft werden, bsp. degree dürfen nicht leer
	public void addThesis(ThesisDTO thesis, Supervisor supervisor) {
		List<Degree> degreeList = degreeRepository.findAllById(thesis.getPossibleDegrees().stream()
				.map(degree -> degree.getId()).toList());
		//todo check if supervisor and degree are at the same university.
		Thesis newThesis = new Thesis(thesis.getName(), thesis.getTask(), thesis.getMotivation(), thesis.getQuestions()
				,thesis.getSupervisingProfessor(), supervisor);
		List<ThesesForDegree> possibleDegrees = new ArrayList<>();
		for(Degree degree : degreeList){
			possibleDegrees.add(new ThesesForDegree(degree, newThesis));
		}
		newThesis.setPossibleDegrees(possibleDegrees);
		//todo set possible theses in degree
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

	public void updateThesis(Thesis thesis, UUID id) {
		Thesis toSave = getThesisById(id);
		toSave.setName(thesis.getName());
		toSave.setTask(thesis.getTask());
		toSave.setMotivation(thesis.getMotivation());
		toSave.setQuestionForm(thesis.getQuestionForm());
		toSave.setSupervisingProfessor(thesis.getSupervisingProfessor());
		toSave.setImages(thesis.getImages());
		toSave.setPossibleDegrees(thesis.getPossibleDegrees());
		thesisRepository.save(toSave);
	}

	public ArrayList<Thesis> getThesesBySupervisor(UUID id){
		return thesisRepository.findBySupervisorId(id);
	}

	public void addImages(ArrayList<MultipartFile> imageFiles, UUID thesisId) throws IOException {
		Thesis thesis = getThesisById(thesisId);
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
		Thesis thesis = getThesisById(thesisId);
		thesis.getImages().clear();
		thesisRepository.save(thesis);
	}
	
	public void deleteThesis(UUID id) {
		thesisRepository.deleteById(id);
	}
}
