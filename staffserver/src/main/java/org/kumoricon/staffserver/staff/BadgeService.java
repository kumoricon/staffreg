package org.kumoricon.staffserver.staff;

import org.kumoricon.staff.StaffBadgePrintFormatter;
import org.kumoricon.staff.badgelib.StaffBadgeDTO;
import org.kumoricon.staffserver.badgeimage.BadgeImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class BadgeService {
    private static final Logger log = LoggerFactory.getLogger(BadgeImageService.class);
    @Value("${staffreg.printing.enabled}")
    private Boolean enablePrinting;
    @Value("${staffreg.file.badgeresourcepath}")
    private String badgeResourcePathString;
    @Value("${staffreg.badgetemplatefilename}")
    private String badgeTemplateFilename;
    private Path badgeResourcePath;
    private File badgeTemplateFile = null;

    private File fontFile;
    @Value("${staffreg.fontfilename}")
    private String fontFileString;

    private final BadgeImageService badgeImageService;
    private Image defaultBadgeImage;

    @Autowired
    public BadgeService(BadgeImageService badgeImageService) {
        this.badgeImageService = badgeImageService;
        if (enablePrinting == null) enablePrinting = true;
    }


    InputStream buildBadge(Staff staff, int xOffset, int yOffset) {
        Image badgeImage;
        if (staff.getHasBadgeImage()) {
            try (InputStream image = new FileInputStream(badgeImageService.getFileFor(staff.getUuid() + "." + staff.getBadgeImageFileType()))) {
                badgeImage = ImageIO.read(image);
            } catch (IOException ex) {
                log.warn("Error loading badge image, using default", ex);
                badgeImage = defaultBadgeImage;
            }
        } else {
            badgeImage = defaultBadgeImage;
        }
        StaffBadgeDTO badgeDTO = staffToStaffBadgeDTO(staff, badgeImage);
        StaffBadgePrintFormatter formatter = new StaffBadgePrintFormatter(badgeDTO, badgeTemplateFile, fontFile, xOffset, yOffset);
        return formatter.getStream();
    }

    @PostConstruct
    private void loadDefaultImage() {
        File defaultImageFile = badgeImageService.getMascotFile();
        try (InputStream image = new FileInputStream(defaultImageFile)) {
            defaultBadgeImage = ImageIO.read(image);
        } catch (IOException e) {
            log.error("Couldn't load {}", defaultImageFile.toPath().toAbsolutePath(), e);
            throw new RuntimeException("Couldn't load default badge image", e);
        }
    }

    private static StaffBadgeDTO staffToStaffBadgeDTO(Staff staff, Image badgeImage) {
        return new StaffBadgeDTO.Builder()
                .withFirstName(staff.getFirstName())
                .withLastName(staff.getLastName())
                .withAgeBackgroundColor(getAgeColorCode(staff.getAgeCategoryAtCon()))
                .withAgeRange(staff.getAgeCategoryAtCon())
                .withDepartment(staff.getDepartment())
                .withDepartmentBackgroundColor(staff.getDepartmentColorCode())
                .withHasBadgeImage(staff.getHasBadgeImage())
                .withPositions(staff.getPositionsArray())
                .withBadgeImage(badgeImage)
                .build();
    }

    private static String getAgeColorCode(String ageCategory) {
        String ageToMatch = ageCategory.toLowerCase();
        switch (ageToMatch) {
            case "adult":
                return "#323E99";
            case "youth":
                return "#FFFF00";
            default:
                return "#CC202A";
        }
    }


    @PostConstruct
    public void createDirectories() {
        try {
            badgeResourcePath = Files.createDirectories(Paths.get(badgeResourcePathString));
            log.info("Badge resource path: " + badgeResourcePath.toAbsolutePath().toString());
        } catch (IOException ex) {
            log.error("Error creating directory {}", badgeResourcePathString, ex);
        }

        if (badgeTemplateFilename != null && !badgeTemplateFilename.trim().isEmpty()) {
            Path filePath = Paths.get(badgeResourcePathString, badgeTemplateFilename);
            badgeTemplateFile = filePath.toFile();
        }

        if (fontFileString != null && !fontFileString.trim().isEmpty()) {
            Path filePath = Paths.get(badgeResourcePathString, fontFileString);
            fontFile = filePath.toFile();
        }
    }

}

