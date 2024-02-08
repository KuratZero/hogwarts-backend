package ru.itmo.wp.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client client;
    private final S3Presigner presigner;

    @Value("${s3.bucket.avatar}")
    private String BUCKET_NAME;

    @PreDestroy
    public void destroy() {
        client.close();
        presigner.close();
    }

    @PostConstruct
    public void initBucket() {
        CreateBucketRequest createAvaBucket = CreateBucketRequest.builder()
                .bucket(BUCKET_NAME).build();
        try {
            client.createBucket(createAvaBucket);
        } catch (BucketAlreadyExistsException ignored) {
        } catch (S3Exception e) {
            System.err.println("WARN creating bucket" + e.getMessage());
        }
    }

    public void deleteAvatarById(Long id) {
        client.deleteObject(DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME).key(id.toString()).build());
    }

    public void updateAvatarById(Long id, byte[] avatar) {
        deleteAvatarById(id);
        client.putObject(
                PutObjectRequest.builder().bucket(BUCKET_NAME)
                        .key(id.toString()).build(),
                RequestBody.fromBytes(avatar));
    }

    public Boolean avatarIsPresent(Long id) {
        try {
            client.headObject(HeadObjectRequest.builder()
                    .bucket(BUCKET_NAME).key(id.toString()).build());
        } catch (NoSuchKeyException ignored) {
            return false;
        }
        return true;
    }

    public String getAvatarById(Long id) {
        if (!avatarIsPresent(id)) {
            return null;
        }
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME).key(id.toString()).build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)).getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);

        return presignedGetObjectRequest.url().toString();
    }
}
