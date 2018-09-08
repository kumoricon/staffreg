package org.kumoricon.staffserver.imageupload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

@Service
public class FileStorageService {
    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);
    @Value("${staffreg.file.uploaddir}")
    private String uploadPathString;

    private Path uploadPath;

    String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.uploadPath.resolve(Instant.now().toEpochMilli() + "-" + fileName);
            Files.copy(file.getInputStream(), targetLocation);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @PostConstruct
    public void createDirectories() {
        try {
            uploadPath = Files.createDirectories(Paths.get(uploadPathString));
            log.info("Impage upload path: " + uploadPath.toAbsolutePath().toString());
        } catch (IOException ex) {
            log.error("Error creating directory", ex);
        }
    }


}
