package ru.itmo.wp.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class S3Config {
    @Value("${s3.iphost}")
    private String IPHOST;

    @Value("${s3.region}")
    private String REGION;

    @Bean
    public S3Client getS3Client() {
        return S3Client.builder().region(Region.of(REGION))
                .endpointOverride(URI.create(IPHOST)).build();
    }

    @Bean
    public S3Presigner getS3Presigner() {
        return S3Presigner.builder().region(Region.of(REGION))
                .endpointOverride(URI.create(IPHOST)).build();
    }
}
