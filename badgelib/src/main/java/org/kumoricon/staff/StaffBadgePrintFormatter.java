package org.kumoricon.staff;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import org.kumoricon.staff.badgelib.StaffBadgeDTO;
import org.kumoricon.staff.badgelib.badgeimage.BadgeCreator;
import org.kumoricon.staff.badgelib.badgeimage.BadgeCreatorStaff2018Back;
import org.kumoricon.staff.badgelib.badgeimage.BadgeCreatorStaff2018Front;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;


public class StaffBadgePrintFormatter{
    private final ByteArrayOutputStream os = new ByteArrayOutputStream();

    private final BadgeCreator badgeCreatorFront = new BadgeCreatorStaff2018Front();
    private final BadgeCreator badgeCreatorBack = new BadgeCreatorStaff2018Back();
    private static final Logger log = LoggerFactory.getLogger(StaffBadgePrintFormatter.class);
    private Integer xOffset = 0;
    private Integer yOffset = 0;

    /**
     * Generates a PDF containing badge ready to be printed
     */
    public StaffBadgePrintFormatter(StaffBadgeDTO attendee, File backgroundFile, File fontFile, int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        PDDocument document = null;
        PDDocument templateDocument = null;

        Font font = null;
        if (fontFile != null) {
            try (InputStream fontStream = new FileInputStream(fontFile)) {
                font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            } catch (FontFormatException | IOException e) {
                log.warn("Error loading font {}, using default", fontFile, e);
            }
        }

        try {
            document = new PDDocument();
            PDPage frontBackground;
            PDPage backBackground;

            if (backgroundFile == null) {
                frontBackground = document.importPage(new PDPage(new PDRectangle(396f, 612f)));
                backBackground = document.importPage(new PDPage(new PDRectangle(396f, 612f)));
            } else {
                templateDocument = PDDocument.load(backgroundFile);
                frontBackground = document.importPage(templateDocument.getPage(0));
                backBackground = document.importPage(templateDocument.getPage(1));
            }

            byte[] badgeImage = badgeCreatorFront.createBadge(attendee, font);
            byte[] badgeImageBack = badgeCreatorBack.createBadge(attendee, font);

            PDPageContentStream contentStream = new PDPageContentStream(document, frontBackground, PDPageContentStream.AppendMode.APPEND, true, false);
            PDImageXObject pdi = PDImageXObject.createFromByteArray(document, badgeImage, attendee.getFirstName()+attendee.getLastName() + ".png");
            contentStream.drawImage(pdi,45+xOffset,81+yOffset, 306, 450);
            contentStream.close();

            PDPageContentStream contentStreamBack = new PDPageContentStream(document, backBackground, PDPageContentStream.AppendMode.APPEND, true, false);
            PDImageXObject pdiBack = PDImageXObject.createFromByteArray(document, badgeImageBack, attendee.getFirstName()+attendee.getLastName() + ".png");
            contentStreamBack.drawImage(pdiBack,45+xOffset,81+yOffset, 306, 450);
            contentStreamBack.close();

            document.save(os);
            document.close();

        } catch (IOException e) {
            log.error("Error creating badge", e);
            throw new RuntimeException(e);
        } finally {
            tryCloseDocument(document);
            tryCloseDocument(templateDocument);
        }
    }


    private static void tryCloseDocument(PDDocument document) {
        if (document != null) {
            try {
                document.close();
            } catch (IOException ignored) {
            }
        }
    }


    public InputStream getStream() {
        return new ByteArrayInputStream(os.toByteArray());
    }

}
