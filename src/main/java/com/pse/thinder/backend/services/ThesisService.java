package com.pse.thinder.backend.services;

import java.util.UUID;

import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotFoundException;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.pse.thinder.backend.repositories.ThesisRepository;

@Service
public class ThesisService {

	private static final String THESIS_NOT_FOUND = "Thesis not found";
	@Autowired
	private ThesisRepository thesisRepository;
	
	public Thesis getThesisById(UUID id) {
		return thesisRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(THESIS_NOT_FOUND));
	}
	
	public void addThesis(Thesis thesis) {
		Thesis toSave = new Thesis(thesis.getName(), thesis.getDescription(), thesis.getQuestionForm());
		
		thesisRepository.save(toSave);
	}
	
	public void updateThesis(Thesis thesis, UUID id) {
		Thesis toSave = getThesisById(id);
		toSave.setName(thesis.getName());
		toSave.setDescription(thesis.getDescription());
		toSave.setQuestionForm(thesis.getQuestionForm());
		
		thesisRepository.save(toSave);
	}
	
	public void deleteThesis(UUID id) {
		thesisRepository.deleteById(id);
	}
}
