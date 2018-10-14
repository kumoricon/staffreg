package org.kumoricon.staff.badgelib.badgeimage;


import org.kumoricon.staff.badgelib.StaffBadgeDTO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BadgeCreatorStaff2018Front implements BadgeCreator {
    private static final int DPI = 300;
    private static final int BADGE_WIDTH = (int) 4.25*DPI;      // 4x6 inch badge with overprint
    private static final int BADGE_HEIGHT = (int) 6.25*DPI;

    @Override
    public byte[] createBadge(StaffBadgeDTO attendee) {
        BadgeImage b = new BadgeImage(BADGE_WIDTH, BADGE_HEIGHT, DPI);

        drawBadgeTypeStripe(b, attendee);
        drawAgeColorStripe(b, attendee);
        drawName(b, attendee);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(b.getImage(), "png", baos);
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void drawAgeColorStripe(BadgeImage b, StaffBadgeDTO attendee) {
        Rectangle ageBackground = new Rectangle(0, 1560, BADGE_WIDTH, 225);
        Color bgColor = Color.decode(attendee.getAgeBackgroundColor());
        Color fgColor = BadgeImage.getInverseColor(bgColor);
        b.fillRect(ageBackground, bgColor);

        b.drawStretchedCenteredString("STAFF", ageBackground, nameFont(), fgColor);
//        Image image = null;
//        try {
//            if (attendee.isAdult()) {
//                image = ImageIO.read(new File("work/badgeresources/staffbadge2018adultfront.png"));
//            } else if (attendee.isYouth()) {
//                image = ImageIO.read(new File("work/badgeresources/staffbadge2018youthfront.png"));
//            } else {
//                image = ImageIO.read(new File("work/badgeresources/staffbadge2018childfront.png"));
//            }
//        } catch (IOException e) {
//
//        }
//        if (image != null) {
//            b.drawImage(image, 0, 1500);
//        }

    }

    private void drawName(BadgeImage b, StaffBadgeDTO attendee) {
        Rectangle nameBg = new Rectangle(235, 1250, 650, 300);
        String[] names = {attendee.getFirstName(), attendee.getLastName()};
        b.drawCenteredStrings(names, nameBg, nameFont(), Color.BLACK);
    }

    private static void drawBadgeTypeStripe(BadgeImage b, StaffBadgeDTO attendee) {
        if (attendee != null) {
            Color bgColor = Color.decode(attendee.getDepartmentBackgroundColor());
            Color fgColor = BadgeImage.getInverseColor(bgColor);
            Rectangle positionsBackground = new Rectangle(18, 0, 200, 1575);
            b.fillRect(positionsBackground, bgColor);

            Rectangle textBounds = new Rectangle(50, 50, 150, 1475);
            b.drawRotatedCenteredStrings(attendee.getPositions(), textBounds, nameFont(), fgColor, true);
        }
    }


    private static Font nameFont() {
        Font f = new Font("Dialog", Font.BOLD, 36);
        return f;
    }
}
