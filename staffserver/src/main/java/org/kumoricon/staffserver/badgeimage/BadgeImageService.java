package org.kumoricon.staffserver.badgeimage;

import org.kumoricon.staffserver.imageupload.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.misc.Resource;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class BadgeImageService {
    private static final Logger log = LoggerFactory.getLogger(BadgeImageService.class);
    @Value("${staffreg.file.badgeimagepath}")
    private String badgeImagePathString;

    private Path badgeImagePath;

//    String getFile(String filename) {
//        String fName = StringUtils.cleanPath(filename);
//
//        try {
//            if(fName.contains("..")) {
//                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fName);
//            }
//
//            Path targetLocation = this.badgeImagePath.resolve(Instant.now().toEpochMilli() + "-" + fileName);
//            Files.copy(file.getInputStream(), targetLocation);
//
//            return fileName;
//        } catch (IOException ex) {
//            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
//        }
//    }

    List<String> availableFiles(final Long timestampMS) {
        if (timestampMS < 0) {
            throw new RuntimeException("Error: timestampMS must be >= 0");
        }
        List<String> files = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(badgeImagePath, "*.{png,jpg}" )) {
            for (Path path : stream) {
                if (path.toFile().lastModified() >= timestampMS) {
                    files.add(path.getFileName().toString());
                }
            }
        } catch (IOException ex) {
            log.error("Error getting available badge images", ex);
        }

        return files;

    }

    @PostConstruct
    public void createDirectories() {
        try {
            badgeImagePath = Files.createDirectories(Paths.get(badgeImagePathString));
            log.info("Badge image path: " + badgeImagePath.toAbsolutePath().toString());
        } catch (IOException ex) {
            log.error("Error creating directory", ex);
        }
    }

    File getFileFor(String fileName) {
        Path filePath = Paths.get(badgeImagePathString, fileName);
        return filePath.toFile();
    }
}
