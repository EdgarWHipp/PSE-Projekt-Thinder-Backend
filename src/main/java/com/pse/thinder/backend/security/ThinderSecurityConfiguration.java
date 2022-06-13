package com.pse.thinder.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ThinderSecurityConfiguration {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//Only HTTPS requests
		http.requiresChannel()
			//Only works on Heroku
			.requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
			.requiresSecure();
		//TODO public endpoints
		http.authorizeRequests()
			.anyRequest().authenticated().and().httpBasic();
		return http.build();
	}
	
	//TODO
	@Bean
	public PasswordEncoder getPasswordEncoder() { return NoOpPasswordEncoder.getInstance(); };
}
