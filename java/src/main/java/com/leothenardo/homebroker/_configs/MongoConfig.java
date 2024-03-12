package com.leothenardo.homebroker._configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;


@Configuration
public class MongoConfig {
	@Value("${spring.data.mongodb.uri}")
	private String MONGO_URI;

	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoDbFactory());
	}

	@Bean
	public SimpleMongoClientDatabaseFactory mongoDbFactory() {
		return new SimpleMongoClientDatabaseFactory(MONGO_URI);
	}
}