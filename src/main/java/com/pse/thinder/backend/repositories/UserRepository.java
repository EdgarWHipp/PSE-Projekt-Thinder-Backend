package com.pse.thinder.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pse.thinder.backend.database.features.account.User;

public interface UserRepository extends JpaRepository<User, Long>{

	public Optional<User> findByMail(String Mail);
}
