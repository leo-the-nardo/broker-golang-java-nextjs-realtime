package com.leothenardo.homebroker.users.repositories;

import com.leothenardo.homebroker.users.entities.ResetPasswordToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ResetPasswordTokenRepository extends MongoRepository<ResetPasswordToken, String> {
	Optional<ResetPasswordToken> findByToken(String token);

	Optional<ResetPasswordToken> findByUserId(String userId);
}
