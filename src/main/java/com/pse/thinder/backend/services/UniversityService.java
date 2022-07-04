package com.pse.thinder.backend.services;

import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.repositories.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UniversityService {
    @Autowired
    UniversityRepository universityRepository;

    public void addUniversity(University uni) {
        universityRepository.save(uni);
    }

    public University getUniversityById(UUID id) {
        return universityRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("")); //todo
    }

}
