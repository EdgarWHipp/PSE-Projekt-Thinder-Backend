package com.pse.thinder.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This class defines beans needed for Spring Security.
 * These are the SecurityFilterChain which is used to apply basic security to all requests
 * and an PasswordEncoder for used for authentification
 *
 */
//Tells Spring Boot that this class is a source for Beans
@Configuration
//Enables the use of Annotations to secure requests endpoints at the definition
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ThinderSecurityConfiguration {

	/**
	 * This method is invoked by Spring Boot
	 * it defines the SecurityFilterChain for Spring Security
	 * @param http given by Spring
	 * @return the custom SecurityFilterChain
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//Only HTTPS requests
		http.requiresChannel()
			//Only works on Heroku
			.requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
			.requiresSecure();

		http.authorizeRequests()
		//Public
			.antMatchers(HttpMethod.GET, "/helloOpen").permitAll()
			.antMatchers(HttpMethod.GET, "/users/verify").permitAll()
			.antMatchers(HttpMethod.GET, "/users/resetPassword").permitAll()
			.antMatchers(HttpMethod.POST, "/users/resetPassword").permitAll()
			.antMatchers(HttpMethod.POST, "/users").permitAll()
			.antMatchers(HttpMethod.POST, "/university").permitAll() // todo remove in production
		//All others Secured
			.anyRequest().authenticated().and().httpBasic();

		http.headers().frameOptions().disable(); // todo added for inmem db console access
		http.csrf().disable(); //todo without this http post doesnt work
		return http.build();
	}
	
	//TODO Plain text passworsd only for Testing 
	/**
	 * This method is invoked by Spring Boot
	 * it defines the PasswordEncoder which is used by Spring Security to authenticate Requests
	 * @return the custom PasswordEncoder
	 */
	@Bean
	public PasswordEncoder getPasswordEncoder() { return NoOpPasswordEncoder.getInstance(); };
}
