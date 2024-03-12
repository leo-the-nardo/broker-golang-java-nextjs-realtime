package com.leothenardo.homebroker.users.repositories;

import com.leothenardo.homebroker.users.entities.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {
	Optional<VerificationToken> findByToken(String token);

	Optional<VerificationToken> findByUsername(String username);
}
