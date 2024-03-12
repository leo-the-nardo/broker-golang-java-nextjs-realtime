package com.leothenardo.homebroker._providers;

import com.leothenardo.homebroker.upload.model.FileReference;

import java.net.URL;

public interface StorageProvider {
	URL generatePresignedUploadUrl(FileReference fileReference);

	boolean fileExists(String filePath);

	void moveFileAsDelete(String fromPath, String toPath);

	String getDownloadUrl(String filePath);
}
