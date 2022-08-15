package com.pse.thinder.backend.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pse.thinder.backend.controllers.errorHandler.GlobalContollerAdvice;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.databaseFeatures.account.User;
import com.pse.thinder.backend.services.UniversityService;
import com.pse.thinder.backend.services.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

	@Mock
	UserService userService;
	
	@InjectMocks
	UserController userController;
	
	MockMvc mockMvc;
	
	private JacksonTester<User> jacksonTester;
	
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(new GlobalContollerAdvice()).build();
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
