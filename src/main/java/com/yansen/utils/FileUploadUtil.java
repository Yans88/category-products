package com.yansen.utils;

import com.yansen.exceptions.ValidationErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FileUploadUtil {

    private static final List<String> contentTypes = Arrays.asList("png", "jpeg", "jpg");
    private final Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);


    /**
     * @param file
     * @param imageDir
     * @return String
     */
    public static String uploadImage(MultipartFile file, String imageDir) {

        String[] filepaths = file.getOriginalFilename().split("\\.");
        if (file.getSize() > 1024 * 1024 * 2) {
            throw new ValidationErrorException("img, file size more than 2Mb");
        }
        if (!contentTypes.contains(filepaths[filepaths.length - 1])) {
            throw new ValidationErrorException("img, unsupported media type");
        }

        String generatedImageName = "";
        if (filepaths.length > 1)
            generatedImageName = UUID.randomUUID() + "." + filepaths[filepaths.length - 1];
        try {
            file.transferTo(new File(imageDir, generatedImageName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return generatedImageName;
    }


}
