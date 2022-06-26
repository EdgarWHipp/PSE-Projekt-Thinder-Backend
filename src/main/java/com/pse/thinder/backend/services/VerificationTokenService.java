package com.pse.thinder.backend.services;

import com.pse.thinder.backend.database.features.VerificationToken;
import com.pse.thinder.backend.repositories.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public void saveVerificationToken(VerificationToken verificationToken){
        verificationTokenRepository.save(verificationToken);
    }

    public Optional<VerificationToken> getToken(String token){
        return verificationTokenRepository.findByToken(token);
    }
}
