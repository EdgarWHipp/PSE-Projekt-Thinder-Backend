package com.pse.thinder.backend.controllers;

import java.util.UUID;

import com.pse.thinder.backend.databaseFeatures.InputValidation;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import com.pse.thinder.backend.security.ThinderUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import com.pse.thinder.backend.services.ThesisService;

@RestController("thesisController")
@RequestMapping("/thesis")
public class ThesisController {
	
	@Autowired
	private ThesisService thesisService;
	
	@Secured("SUPERVISOR")
	@PostMapping()
	public void postThesis(@Validated(InputValidation.class) @RequestBody Thesis thesis) {
		ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
	            getContext().getAuthentication().getPrincipal();
		thesisService.addThesis(thesis, (Supervisor) details.getUser());
	}
	
	@GetMapping("/{id}")
	public Thesis getThesis(@PathVariable("id") UUID id) {
		return thesisService.getThesisById(id);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("@thesisController.currentUserIsThesisOwner(#id)")
	public void putThesis(@PathVariable("id") UUID id, @Validated(InputValidation.class) @RequestBody Thesis thesis) {
		thesisService.updateThesis(thesis, id);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("@thesisController.currentUserIsThesisOwner(#id)")
	public void deleteThesis(@PathVariable("id") UUID id) {
		thesisService.deleteThesis(id);
	}
	
	public boolean currentUserIsThesisOwner(UUID thesisId) {
		ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
	            getContext().getAuthentication().getPrincipal();
		return thesisService.getThesisById(thesisId).getSupervisor().getId().equals(details.getUser().getId());
	}
}
