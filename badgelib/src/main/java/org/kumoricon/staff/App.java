package org.kumoricon.staff;

import com.sun.management.UnixOperatingSystemMXBean;
import org.kumoricon.staff.badgelib.StaffBadgeDTO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class App
{
    public static void main( String[] args )
    {
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        if (os instanceof UnixOperatingSystemMXBean) {
            System.out.println("Number of open fd: " + ((UnixOperatingSystemMXBean) os).getOpenFileDescriptorCount());
        }

//        b.addAgeRange("Adult", 18, 255, adultCost, "#323E99", "Adult");
//        b.addAgeRange("Youth", 13, 17, youthCost, "#FFFF00", "Youth");
//        b.addAgeRange("Child", 6, 12, childCost, "#CC202A", "Child");

        try (InputStream chibi = new BufferedInputStream(new FileInputStream(new File("2018mascot.png")))) {
            Image staffImage = ImageIO.read(chibi);

            StaffBadgeDTO staff = new StaffBadgeDTO.Builder()
                    .withFirstName("Jason")
                    .withLastName("Short")
                    .withAgeBackgroundColor("#323E99")
                    .withAgeRange("Adult")
                    .withDepartment("Registration")
                    .withDepartmentBackgroundColor("#f57f20")
                    .withHasBadgeImage(false)
                    .withPosition("Registration Software Development Manager")
//                .withPosition("Grand Poobah")
                    .withPosition("Dude")
                    .withBadgeImage(staffImage)
                    .build();

            File template = new File("staffbadge.pdf");

            StaffBadgePrintFormatter formatter = new StaffBadgePrintFormatter(staff, template, 0, 0);

            try {
                InputStream inputStream = formatter.getStream();
                byte[] buffer = new byte[inputStream.available()];

                inputStream.read(buffer);

                File targetFile = new File("output.pdf");
                OutputStream outStream = new FileOutputStream(targetFile);
                outStream.write(buffer);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

            System.out.println( "Hello World!" );
            System.out.println(staff);

            if (os instanceof UnixOperatingSystemMXBean) {
                System.out.println("Number of open fd: " + ((UnixOperatingSystemMXBean) os).getOpenFileDescriptorCount());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
