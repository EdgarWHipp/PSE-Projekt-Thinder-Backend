package com.pse.thinder.backend.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pse.thinder.backend.database.features.thesis.Thesis;
import com.pse.thinder.backend.repositories.ThesisRepository;

@Service
public class ThesisService {

	@Autowired
	private ThesisRepository thesisRepository;
	
	public Thesis getThesisById(UUID id) {
		//TODO ThesisNotFoundException
		return thesisRepository.findById(id).orElseThrow(() -> new RuntimeException());
	}
	
	public void addThesis(Thesis thesis) {
		//TODO
	}
	
	public void updateThesis(Thesis thesis, UUID id) {
		//TODO
	}
	
	public void deleteThesis(UUID id) {
		thesisRepository.deleteById(id);
	}
}
