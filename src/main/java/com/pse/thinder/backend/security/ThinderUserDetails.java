package com.pse.thinder.backend.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pse.thinder.backend.databaseFeatures.account.User;

/**
 * This class represents an User for the authentification by Spring Security
 *
 */
public class ThinderUserDetails implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7949393278192849178L;

	private final User user;
	
	/**
	 * Creates a new Instance for the given User
	 * @param user
	 */
	public ThinderUserDetails(User user) {
		this.user = user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getMail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isActive();
	}

	/**
	 * Returns the user for more details not needed by Spring Security
	 * @return the user which this class represents
	 */
	public User getUser() {
		return user;
	}

}
