package com.pse.thinder.backend.controllers;

import java.util.UUID;

import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import com.pse.thinder.backend.services.ThesisService;

@RestController
@RequestMapping("/thesis")
public class ThesisController {
	
	@Autowired
	private ThesisService thesisService;
	
	@PostMapping()
	public void postThesis(@RequestBody Thesis thesis) {
		thesisService.addThesis(thesis);
	}
	
	@GetMapping("/{id}")
	public Thesis getThesis(@PathVariable("id") UUID id) {
		return thesisService.getThesisById(id);
	}
	
	@PutMapping("/{id}")
	public void putThesis(@PathVariable("id") UUID id, @RequestBody Thesis thesis) {
		thesisService.updateThesis(thesis, id);
	}
	
	@DeleteMapping("/{id}")
	public void deleteThesis(@PathVariable("id") UUID id) {
		thesisService.deleteThesis(id);
	}
}
