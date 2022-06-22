package com.pse.thinder.backend.restController;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pse.thinder.backend.security.ThinderUserDetails;

@RestController
public class TestController {

	@GetMapping("/helloProtected")
	public String hello() {
		ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return String.format("Hello %s", details.getUser().getFirstName());
	}

	@GetMapping("/helloOpen")
	public String helloOpen() {
		return "Hello world!";
	}
}
