package com.yansen.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.yansen.exceptions.ValidationErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Service
public class S3BucketStorage {

    private static final List<String> contentTypes = Arrays.asList("png", "jpeg", "jpg");
    private final Logger logger = LoggerFactory.getLogger(S3BucketStorage.class);
    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${bucketnames3}")
    private String bucketName;

    /**
     * Upload file into AWS S3
     *
     * @param file
     * @return String
     */

    public String uploadIMG(MultipartFile file, String folder, String name) {
        String[] filepaths = file.getOriginalFilename().split("\\.");
        if (file.getSize() > 1024 * 1024 * 2) {
            throw new ValidationErrorException("img, file size more than 2Mb");
        }
        if (!contentTypes.contains(filepaths[filepaths.length - 1])) {
            throw new ValidationErrorException("img, unsupported media type");
        }
        name = name.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
        String keyName = "";
        if (filepaths.length > 1)
            keyName = name + "_" + System.currentTimeMillis() + "." + filepaths[filepaths.length - 1];
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            metadata.setContentDisposition("inline");
            amazonS3Client.putObject(bucketName, folder + "/" + keyName, file.getInputStream(), metadata);
            URL s3Url = amazonS3Client.getUrl(bucketName, folder + "/" + keyName);
            return s3Url.toExternalForm();
        } catch (IOException ioe) {
            logger.error("IOException: " + ioe.getMessage());
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException: " + serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }
        return keyName;
    }

}