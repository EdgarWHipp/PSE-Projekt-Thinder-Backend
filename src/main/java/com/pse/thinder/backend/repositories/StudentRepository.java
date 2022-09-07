package com.pse.thinder.backend.repositories;

import com.pse.thinder.backend.databaseFeatures.account.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * 
 * Repository to access the Student table of the database
 *
 */
public interface StudentRepository extends JpaRepository<Student, UUID> {
}
