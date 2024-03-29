package com.leothenardo.homebroker._providers;


import com.leothenardo.homebroker._configs.properties.StorageProperties;
import com.leothenardo.homebroker.upload.exceptions.StorageCloudException;
import com.leothenardo.homebroker.upload.model.FileReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.awscore.AwsRequestOverrideConfiguration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@Component
public class S3StorageProvider implements StorageProvider {
	private final static Logger log = LoggerFactory.getLogger(S3StorageProvider.class);
	private final S3Presigner s3Presigner;
	private final S3Client s3Client;
	private final StorageProperties storageProperties;


	public S3StorageProvider(S3Presigner s3Presigner, S3Client s3Client, StorageProperties storageProperties) {
		this.s3Presigner = s3Presigner;
		this.s3Client = s3Client;
		this.storageProperties = storageProperties;
	}

	@Override
	public URL generatePresignedUploadUrl(FileReference fileReference) {
		var builder = AwsRequestOverrideConfiguration.builder();
		if (fileReference.isPublicAccessible()) { // for some reason, SDK doesn't add this automatically at .acl() method
			builder.putRawQueryParameter("x-amz-acl", "public-read");
		}
		PutObjectRequest objReq = PutObjectRequest
						.builder()
						.bucket(getBucket())
						.key(fileReference.getPath())
						.contentType(fileReference.getContentType())
						.contentLength(fileReference.getContentLength())
						.acl(fileReference.isPublicAccessible() ? "public-read" : null)
						.overrideConfiguration(builder.build())
						.build();
		System.out.println(objReq.toString());
		PutObjectPresignRequest presignRequest = PutObjectPresignRequest
						.builder()
						.signatureDuration(Duration.ofMinutes(30))
						.putObjectRequest(objReq)
						.build();
		System.out.println(presignRequest.toString());
		return s3Presigner.presignPutObject(presignRequest).url();
	}

	@Override
	public boolean fileExists(String filePath) {
		if (filePath == null || filePath.isBlank()) {
			return false;
		}
		HeadObjectRequest request = HeadObjectRequest.builder()
						.bucket(getBucket())
						.key(filePath)
						.build();
		try {
			s3Client.headObject(request);
			return true;

		} catch (S3Exception e) {
			if (e.statusCode() == 404) {
				return false;
			}
			throw e;
		}
	}

	public String getDownloadUrl(String filePath) {
		GetUrlRequest request = GetUrlRequest.builder()
						.bucket(getBucket())
						.key(filePath)
						.build();
		return s3Client.utilities().getUrl(request).toExternalForm();
	}

	public void moveFileAsDelete(String fromPath, String toPath) {
		CopyObjectRequest copyObjReq = CopyObjectRequest.builder()
						.sourceBucket(getBucket())
						.destinationBucket(getBucket())
						.sourceKey(fromPath)
						.destinationKey(toPath)
						.storageClass(StorageClass.DEEP_ARCHIVE)
						.build();

		try {
			s3Client.copyObject(copyObjReq);
		} catch (S3Exception e) {
			log.error(String.format("Error moving file from %s to %s", fromPath, toPath), e);
			throw new StorageCloudException(String.format("Error moving file from %s to %s", fromPath, toPath));
		}
		removeFile(fromPath);
	}

	private void removeFile(String filePath) {
		DeleteObjectRequest deleteObjReq = DeleteObjectRequest.builder()
						.bucket(getBucket())
						.key(filePath)
						.build();
		try {
			s3Client.deleteObject(deleteObjReq);
		} catch (S3Exception e) {
			log.error(String.format("Error removing file %s", filePath), e);
			throw new StorageCloudException(String.format("Error removing file %s", filePath));
		}
	}

	private String getBucket() {
		return storageProperties.getS3().getBucketName();
	}
}
