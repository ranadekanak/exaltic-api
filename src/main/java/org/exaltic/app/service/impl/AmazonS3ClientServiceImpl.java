package org.exaltic.app.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.exaltic.app.service.AmazonS3ClientService;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

@Service
public class AmazonS3ClientServiceImpl implements AmazonS3ClientService {

	private static final String SUFFIX = "/";
	private static final Regions REGION = Regions.AP_SOUTH_1;
	private static AmazonS3 S3_CLIENT;

	static {
		AWSCredentials credentials = new BasicAWSCredentials("","");
		S3_CLIENT = AmazonS3ClientBuilder.standard()
									.withCredentials(new AWSStaticCredentialsProvider(credentials))
									.withRegion(Regions.AP_SOUTH_1).build();
	}

	@Override
	public void createBucket(String bucketName) {
		if (S3_CLIENT.doesBucketExist(bucketName)) {
			return;
		}
		CreateBucketRequest request = new CreateBucketRequest(bucketName, REGION.getName());
		S3_CLIENT.createBucket(request);
	}

	@Override
	public List<String> listBuckets() {
		List<Bucket> buckets = S3_CLIENT.listBuckets();
		return buckets.stream().map(e -> e.getName()).collect(Collectors.toList());
	}

	@Override
	public ObjectMetadata uploadFileToBucket(String bucketName, String uploadKey, InputStream inputStream) {
		createBucket(bucketName);
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uploadKey, inputStream,
				new ObjectMetadata());
		putObjectRequest.setCannedAcl(CannedAccessControlList.Private);
		PutObjectResult putObjectResult = S3_CLIENT.putObject(putObjectRequest);
		return putObjectResult.getMetadata();
	}

	@Override
	public S3ObjectInputStream downloadFileFromBucket(String bucketName, String key) {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
		S3Object s3Object = S3_CLIENT.getObject(getObjectRequest);
		return s3Object.getObjectContent();
	}

	@Override
	public ObjectMetadata createEmptyFolderInBucket(String bucketName, String folderName) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + SUFFIX, emptyContent,
				metadata);
		// send request to S3 to create folder
		return S3_CLIENT.putObject(putObjectRequest).getMetadata();
	}

}
