package com.leothenardo.homebroker.users.repositories;

import com.leothenardo.homebroker.users.entities.TwoFactorToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TwoFactorRepository extends MongoRepository<TwoFactorToken, String> {
	TwoFactorToken findByUsername(String username);

	TwoFactorToken findByToken(String token);

}
