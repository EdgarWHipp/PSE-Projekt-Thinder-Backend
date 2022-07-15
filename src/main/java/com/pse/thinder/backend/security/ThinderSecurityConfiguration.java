package com.pse.thinder.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
			.antMatchers(HttpMethod.GET,"/users/current/getRole").authenticated().and().httpBasic();
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET,"/users/current").authenticated().and().httpBasic();
		http.authorizeRequests()
			.antMatchers(HttpMethod.PUT,"/users/current").authenticated().and().httpBasic();







		http.headers().frameOptions().disable(); // todo added for inmem db console access
		http.csrf().disable(); //todo without this http post doesnt work
		return http.build();
	}
	
	//TODO
	@Bean
	public PasswordEncoder getPasswordEncoder() { return NoOpPasswordEncoder.getInstance(); };
}
