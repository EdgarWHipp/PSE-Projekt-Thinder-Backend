package com.pse.thinder.backend.controllers;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pse.thinder.backend.controllers.errorHandler.GlobalContollerAdvice;
import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotFoundException;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.services.UniversityService;

@ExtendWith(MockitoExtension.class)
class UniversityControllerTests {

	@Mock
	UniversityService universityService;
	
	@InjectMocks
	UniversityController universityController;
	
	MockMvc mockMvc;
	
	private JacksonTester<University> jacksonTester;
	
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(universityController).setControllerAdvice(new GlobalContollerAdvice()).build();
		JacksonTester.initFields(this, new ObjectMapper());
	}
	
	@Test
	void testGetUniversityExists() throws Exception {
		University kit = new University("KIT");
		UUID u = UUID.randomUUID();
		BDDMockito.given(universityService.getUniversityById(u)).willReturn(kit);
		MockHttpServletResponse result = mockMvc.perform(MockMvcRequestBuilders.get("/university/"+u)).andReturn().getResponse();
		
		Assertions.assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
		Assertions.assertThat(result.getContentAsString()).isEqualTo(jacksonTester.write(kit).getJson());
	}
	
	@Test
	void testGetUniversityNotExeists() throws Exception {
		UUID u = UUID.randomUUID();
		BDDMockito.given(universityService.getUniversityById(u)).willThrow(new EntityNotFoundException("Test"));
		MockHttpServletResponse result = mockMvc.perform(MockMvcRequestBuilders.get("/university/"+u)).andReturn().getResponse();
		
		Assertions.assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}
}
