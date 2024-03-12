package com.leothenardo.homebroker.users.repositories;

import com.leothenardo.homebroker.users.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends MongoRepository<User, String> {

	User findByUsername(String username);

	User findByProviderId(String providerId);

}
