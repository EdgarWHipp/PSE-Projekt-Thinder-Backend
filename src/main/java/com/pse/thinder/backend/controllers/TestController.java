package com.pse.thinder.backend.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pse.thinder.backend.security.ThinderUserDetails;

/**
 * 
 * Simple rest controller to test spring boot and spring security
 *
 */
@RestController
public class TestController {

	/**
	 * This method users spring security to allow only registered users
	 * @return Returns the name of the authenticated user
	 */
	@GetMapping("/helloProtected")
	public String hello() {
		ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return String.format("Hello %s", details.getUser().getFirstName());
	}

	/**
	 * Returns a simple string for every request
	 * @return "Hello world!"
	 */
	@GetMapping("/helloOpen")
	public String helloOpen() {
		return "Hello world!";
	}
}
