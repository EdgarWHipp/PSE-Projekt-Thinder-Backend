package com.pse.thinder.backend.restController;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pse.thinder.backend.database.features.thesis.Thesis;
import com.pse.thinder.backend.services.ThesisService;

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
}
