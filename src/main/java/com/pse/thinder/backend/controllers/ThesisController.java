package com.pse.thinder.backend.controllers;

import java.util.List;
import java.util.UUID;

import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.dto.ThesisDTO;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;
import com.pse.thinder.backend.security.ThinderUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import com.pse.thinder.backend.services.ThesisService;

@RestController("thesisController")
@RequestMapping("/thesis")
public class ThesisController {

	@Autowired
	private ThesisService thesisService;
	

	@PostMapping()
	@PreAuthorize("hasRole('ROLE_SUPERVISOR')")
	public void postThesis(@RequestBody ThesisDTO thesis) {
		ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
	            getContext().getAuthentication().getPrincipal();
		thesisService.addThesis(thesis, (Supervisor) details.getUser());
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_SUPERVISOR')")
	public ThesisDTO getThesis(@PathVariable("id") UUID id) {
		return thesisService.getThesisById(id);
	}

	@GetMapping
	public List<ThesisDTO> getTheses(){
		ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
				getContext().getAuthentication().getPrincipal();
		return thesisService.getThesesBySupervisor(details.getUser().getId());
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("@thesisController.currentUserIsThesisOwner(#id)")
	public void putThesis(@PathVariable("id") UUID id, @RequestBody ThesisDTO thesis) {
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
		return thesisService.getActualThesisById(thesisId).getSupervisor().getId().equals(details.getUser().getId());
	}
}
