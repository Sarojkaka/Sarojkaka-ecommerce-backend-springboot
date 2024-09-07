package com.store.ecommerce.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileUtility {

    private static final Logger logger = LoggerFactory.getLogger(FileUtility.class);

    public String saveImage(MultipartFile image, String imagePath) {
        try {

            String originalFilename = image.getOriginalFilename();
            String randomID = UUID.randomUUID().toString();
            String fileName = randomID.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
            // Full path where the image will be stored
            String filePath = imagePath + File.separator + fileName;

            // Create the directory if it doesn't exist
            File directory = new File(imagePath);
            if (!directory.exists()) {
                directory.mkdirs();  // Create directories if they don't exist
            }

            // Save the file to the path
            Files.copy(image.getInputStream(), Paths.get(filePath));

            logger.info("Image saved successfully: {}", fileName);
            return fileName;  // Return the file name to store in the product entity
        } catch (IOException e) {
            logger.error("Error saving image: {}", e.getMessage());
            throw new RuntimeException("Failed to save image", e);
        }
    }
}
