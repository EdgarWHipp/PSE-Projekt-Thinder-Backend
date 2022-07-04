package com.pse.thinder.backend.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pse.thinder.backend.databaseFeatures.account.User;

public interface UserRepository extends JpaRepository<User, UUID>{

	public Optional<User> findByMail(String mail);
}
