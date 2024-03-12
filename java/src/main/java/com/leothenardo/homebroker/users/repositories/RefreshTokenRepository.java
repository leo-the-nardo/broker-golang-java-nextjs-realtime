package com.leothenardo.homebroker.users.repositories;

import com.leothenardo.homebroker.users.entities.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

	RefreshToken findByToken(String token);

	RefreshToken findByUserId(String userId);
}
