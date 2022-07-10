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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.services.UniversityService;

@ExtendWith(MockitoExtension.class)
class UniversityControllerTests {

	@Mock
	UniversityService universityService;
	
	@InjectMocks
	UniversityController universityController;
	
	MockMvc mockMvc;
	
	private JacksonTester<University> tester1;
	private JacksonTester<UUID> tester2;
	
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(universityController).build();
		JacksonTester.initFields(this, new ObjectMapper());
	}
	
	@Test
	void testGetUniversity() throws Exception {
		University kit = new University("KIT");
		UUID u = UUID.randomUUID();
		BDDMockito.given(universityService.getUniversityById(u)).willReturn(kit);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/university").param("id", u.toString())).andReturn();
		
		Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(tester1.write(kit).getJson());
	}
}
