package com.myecom.myecomapp.serviceimpl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.myecom.myecomapp.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    public AmazonS3 amazonS3;

    @Value("${aws.s3.bucket.product}")
    private String productBucket;

    @Override
    public String uploadFileS3(MultipartFile file) {
        try {
            String fileName = "product/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3.putObject(new PutObjectRequest(productBucket, fileName, inputStream, metadata));

            return amazonS3.getUrl(productBucket, fileName).toString();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteFileS3(String fileUrl) {
        try {
            if (fileUrl == null || fileUrl.isEmpty()) {
                return false;
            }

            // Full bucket base URL
            String bucketUrl = "https://" + productBucket + ".s3.amazonaws.com/";

            // If URL does not match bucket, do not attempt delete
            if (!fileUrl.startsWith(bucketUrl)) {
                return false;
            }

            // Extract only the S3 key
            String fileKey = fileUrl.substring(bucketUrl.length());

            amazonS3.deleteObject(productBucket, fileKey);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
