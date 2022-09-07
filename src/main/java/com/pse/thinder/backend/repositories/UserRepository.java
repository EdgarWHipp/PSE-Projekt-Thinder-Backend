package com.pse.thinder.backend.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pse.thinder.backend.databaseFeatures.account.User;

/**
 * 
 * Repository to access the User table of the database
 *
 */
public interface UserRepository extends JpaRepository<User, UUID>{

	/**
	 * Autogenerated method to find the user with a specific email if one exists
	 * @param mail the mail of the user
	 * @return the optional for the user
	 */
	public Optional<User> findByMail(String mail);
}
