package com.springboot.springboots3.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class S3Uploader {

    @Value("${cloud.aws.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    public void upload() {
        try {
            BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(region)
                    .build();

            s3Client.putObject(bucket, "key", "uploaded string object");
        } catch(AmazonServiceException e) {
            e.printStackTrace();
            throw new RuntimeException("Amazon S3가 제대로 process 되지 않았습니다");
        } catch(SdkClientException e) {
            e.printStackTrace();
            throw new RuntimeException("Amazon S3의 응답과 연결될 수 없거나, 클라이언트가 응답을 parse 할 수 없습니다.");
        }
    }
}
