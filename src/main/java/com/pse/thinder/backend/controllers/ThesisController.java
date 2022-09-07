package com.pse.thinder.backend.controllers;

import java.util.List;
import java.util.UUID;

import com.pse.thinder.backend.databaseFeatures.ThesisDTO;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;
import com.pse.thinder.backend.security.ThinderUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import com.pse.thinder.backend.services.ThesisService;

/**
 * This class contains all request mapping for creating, updating and deleting thesis.
 *
 */
@RestController("thesisController")
@RequestMapping("/thesis")
public class ThesisController {
	
	@Autowired
	private ThesisService thesisService;
	

	/**
	 * This method creates a new thesis with the data provided by the ThesisDTO.
	 * 
	 * Protected access.
	 * 
	 * @param thesis the data for 
	 */
	@PostMapping()
	@PreAuthorize("hasRole('ROLE_SUPERVISOR')")
	public void postThesis(@RequestBody ThesisDTO thesis) {
		ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
	            getContext().getAuthentication().getPrincipal();
		thesisService.addThesis(thesis, (Supervisor) details.getUser());
	}
	
	/**
	 * This returns the thesis data for a specific thesis id
	 * @param id
	 * @return thesisDTO with the data of the requested thesis
	 * 
	 * Protected access
	 */
	@GetMapping("/{id}")
	public ThesisDTO getThesis(@PathVariable("id") UUID id) {
		return thesisService.getThesisById(id);
	}

	/**
	 * Returns all theses created by the requesting user.
	 * 
	 * Protected access and only for users of type SUPERVISOR
	 * 
	 * @return the theses
	 */
	@GetMapping
	@PreAuthorize("hasRole('ROLE_SUPERVISOR')")
	public List<ThesisDTO> getTheses(){
		ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
				getContext().getAuthentication().getPrincipal();
		return thesisService.getThesesBySupervisor(details.getUser().getId());
	}
	
	
	/**
	 * This updates the thesis with the given id with the supplied thesisDTO data.
	 * 
	 * Protected access, only for users of type SUPERVISOR and the user must own the thesis
	 * 
	 * @param id the id of the thesis to update
	 * @param thesis the new data for the thesis
	 */
	@PutMapping("/{id}")
	@PreAuthorize("@thesisController.currentUserIsThesisOwner(#id)")
	public void putThesis(@PathVariable("id") UUID id, @RequestBody ThesisDTO thesis) {
		thesisService.updateThesis(thesis, id);
	}
	
	/**
	 * This delete the thesis with the given id.
	 * 
	 * Protected access, only for users of type SUPERVISOR and the user must own the thesis
	 * 
	 * @param id the id of the thesis to update
	 * @param thesis the new data for the thesis
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@thesisController.currentUserIsThesisOwner(#id)")
	public void deleteThesis(@PathVariable("id") UUID id) {
		thesisService.deleteThesis(id);
	}
	
	/**
	 * This checks if the thesis with the id belongs to the current user
	 * @param thesisId the if of the thesis
	 * @return true if the thesis belongs to the user false if not
	 */
	public boolean currentUserIsThesisOwner(UUID thesisId) {
		ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
	            getContext().getAuthentication().getPrincipal();
		return thesisService.getActualThesisById(thesisId).getSupervisor().getId().equals(details.getUser().getId());
	}
}
