package com.pse.thinder.backend.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotFoundException;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;
import com.pse.thinder.backend.databaseFeatures.thesis.Image;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import com.pse.thinder.backend.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.pse.thinder.backend.repositories.ThesisRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ThesisService {

	private static final String THESIS_NOT_FOUND = "Thesis not found";
	@Autowired
	private ThesisRepository thesisRepository;

	@Autowired
	private ImageRepository imageRepository;
	
	public Thesis getThesisById(UUID id) {
		return thesisRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(THESIS_NOT_FOUND));
	}

	public void addThesis(Thesis thesis, Supervisor supervisor) {
		Thesis toSave = new Thesis(thesis.getName(), thesis.getDescription(), thesis.getQuestionForm(), supervisor);
		toSave.setImages(thesis.getImages());
		toSave.setPossibleDegrees(thesis.getPossibleDegrees());
		thesisRepository.save(toSave);
	}

	public void updateThesis(Thesis thesis, UUID id) {
		Thesis toSave = getThesisById(id);
		toSave.setName(thesis.getName());
		toSave.setDescription(thesis.getDescription());
		toSave.setQuestionForm(thesis.getQuestionForm());
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
			newImage.setName(imageFile.getName());
			newImage.setImage(imageFile.getBytes());
			newImage.setThesis(thesis);
			newImages.add(newImage);
		}
		thesis.getImages().addAll(newImages);
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
