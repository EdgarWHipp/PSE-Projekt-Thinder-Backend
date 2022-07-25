package com.pse.thinder.backend.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import com.pse.thinder.backend.databaseFeatures.thesis.Image;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.pse.thinder.backend.services.ThesisService;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;

@RestController
public class ThesisController {
	
	@Autowired
	private ThesisService thesisService;
	
	@PostMapping("Thesis")
	public void postThesis(@RequestBody Thesis thesis) {
		thesisService.addThesis(thesis);
	}
	
	@GetMapping("Thesis/{id}")
	public Thesis getThesis(@PathVariable("id") UUID id) {
		return thesisService.getThesisById(id);
	}
	
	@PutMapping("Thesis/{id}")
	public void putThesis(@PathVariable("id") UUID id, @RequestBody Thesis thesis) {
		thesisService.updateThesis(thesis, id);
	}
	
	@DeleteMapping("Thesis/{id}")
	public void deleteThesis(@PathVariable("id") UUID id) {
		thesisService.deleteThesis(id);
	}

	@PostMapping("Supervisor/{id}/Theses") //todo you could change that to get the current id of auth. user
	public ArrayList<Thesis> getThesesForSupervisor(@PathVariable("id") UUID id){
		return thesisService.getThesesBySupervisor(id);
	}

	@PostMapping("Theses/{id}/images")
	public void removeImage(@PathVariable("id") UUID id){
		thesisService.removeImages(id);
	}

	@PostMapping("Theses/{id}/newImages")
	public void addImages(@PathVariable("id") UUID id, @RequestBody ArrayList<MultipartFile> imageFiles) throws IOException {
		thesisService.addImages(imageFiles, id);
	}


}
