package com.pse.thinder.backend.repositories;

import com.pse.thinder.backend.database.features.account.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
}
