package com.pse.thinder.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ThinderSecurityConfiguration {

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
			.antMatchers(HttpMethod.POST, "/users/verify").permitAll()
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
	
	//TODO
	@Bean
	public PasswordEncoder getPasswordEncoder() { return NoOpPasswordEncoder.getInstance(); };
}
