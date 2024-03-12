package com.leothenardo.homebroker._providers;

import com.leothenardo.homebroker._configs.properties.StorageProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {
	@Bean
	public S3Presigner presigner(StorageProperties storageProperties) {
		StorageProperties.S3 s3 = storageProperties.getS3();
		AwsBasicCredentials credentials = AwsBasicCredentials.create(s3.getAccessKey(), s3.getSecretKey());
		StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

		return S3Presigner.builder()
						.region(Region.of(s3.getRegion()))
						.credentialsProvider(credentialsProvider)
						.build();
	}

	@Bean
	public S3Client s3Client(StorageProperties storageProperties) {
		StorageProperties.S3 s3 = storageProperties.getS3();
		AwsBasicCredentials credentials = AwsBasicCredentials.create(s3.getAccessKey(), s3.getSecretKey());
		StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

		return S3Client.builder()
						.region(Region.of(s3.getRegion()))
						.credentialsProvider(credentialsProvider)
						.build();
	}
}
