package com.daemon.kafka.producer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class S3Function {

	Regions clientRegion = Regions.AP_NORTHEAST_2;
    String bucketName = "YOUR_BUCKET_NAME";
    AmazonS3 s3Client = null;


	public S3Function() {
		BasicAWSCredentials credential = new BasicAWSCredentials("<YOUR_AWS_ACCESS_KEY>", "YOUR_AWS_SECRET_KEY");
    	ClientConfiguration clientConf = new ClientConfiguration();
    	
    	/*
    	clientConf.setConnectionTimeout(0);
    	clientConf.setSocketTimeout(0);
    	clientConf.setClientExecutionTimeout(0);
    	clientConf.setRequestTimeout(0);
    	for testing kafka. parameter "0" is not recommend because 0 means infinity
    	
    	*/
    	
    	s3Client = AmazonS3ClientBuilder.standard()
    			.withRegion(clientRegion)
                .withCredentials(new AWSStaticCredentialsProvider(credential))
                .withClientConfiguration(clientConf)
                .build();
	}
	
	public String getObject() throws IOException {
    	
    	
    	S3Object s3object = s3Client.getObject(bucketName, "<YOUR_OBJECT_KEY_NAME>");
    	S3ObjectInputStream inputStream = s3object.getObjectContent();
    	FileUtils.copyInputStreamToFile(inputStream, new File("<KEY_SAVE_LOCATION_IN_YOUR_FILESYSTEM>"));
    	File file = new File("/home/ec2-user/zzzz.txt");
    	InputStream inputstream = new FileInputStream(file);
    	
    	
    	BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
    	
    	
    	String fullContent=null;
    	String line = null;
    	while((line=reader.readLine())!=null){
    		fullContent=fullContent+line;
    	}
    	//System.out.println(fullContent);
    	return fullContent;
    }

}
