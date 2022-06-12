package com.pse.thinder.backend.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pse.thinder.backend.repositories.UserRepository;

import database.features.account.User;

@Service
public class ThinderUserDetailsService implements UserDetailsService{

	private static final String ERROR_MSG = "User not found: ";
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> response = userRepository.findByMail(username);
		User user = response.orElseThrow(() -> new UsernameNotFoundException(ERROR_MSG + username));
		return new ThinderUserDetails(user);
	}

}
