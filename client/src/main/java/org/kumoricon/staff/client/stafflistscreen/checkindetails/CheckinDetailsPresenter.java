package org.kumoricon.staff.client.stafflistscreen.checkindetails;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import org.kumoricon.staff.client.SettingsService;
import org.kumoricon.staff.client.ViewModel;
import org.kumoricon.staff.client.WorkingDirectoryHelper;
import org.kumoricon.staff.client.model.Staff;
import org.kumoricon.staff.client.stafflistscreen.StafflistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class CheckinDetailsPresenter implements Initializable {
    private Staff staff;
    private static final Logger log = LoggerFactory.getLogger(CheckinDetailsPresenter.class);

    @FXML
    ImageView imgWebcam, imgPicture1, imgPicture2, imgSignature;

    @FXML
    Button btnPicture1, btnPicture2, btnSignature, btnClearSignature, btnCheckIn, btnReprint;

    @FXML
    Label lblName, lblLegalName;
    @Inject
    ViewModel viewModel;

    @Inject
    WebcamService webcamService;

    @Inject
    SettingsService settings;
    @Inject
    SigpadService sigpadService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        staff = viewModel.getSelectedStaff();
        lblName.setText(staff.getName());
        lblLegalName.setText(staff.getLegalName());
        setViewState();
        imgWebcam.imageProperty().bind(webcamService.getImageProperty());
        imgSignature.imageProperty().bind(sigpadService.getSigImageProperty());
    }

    private void setImageState() {
        if (staff.isPicture1Saved()) {
            Image image = loadImageFrom(settings.getWorkQueue() + staff.getFilename() + "-1.jpg");
            if (image == null) {
                image = new Image("/images/picture1saved.png");
            }
            imgPicture1.setImage(image);

        } else {
            imgPicture1.setImage(new Image("/images/picture1example.png"));
        }

        if (staff.isPicture2Saved()) {
            Image image = loadImageFrom(settings.getWorkQueue() + staff.getFilename() + "-2.jpg");
            if (image == null) {
                image = new Image("/images/picture2saved.png");
            }
            imgPicture2.setImage(image);
        } else {
            imgPicture2.setImage(new Image("/images/picture2example.png"));
        }

    }


    private void setViewState() {
        log.info("Setting view state");
        if (staff == null) {
            log.warn("Warning: CheckinDetails trying to set view state for null Staff");
            return;
        }
        setImageState();

        try {
            if (staff.isPicture1Saved() && staff.isPicture2Saved() && staff.isSignatureSaved()) {
                btnCheckIn.setDisable(false);
            } else {
                btnCheckIn.setDisable(true);
            }

            if (staff.getCheckedIn()) {
                btnReprint.setDisable(false);
                btnPicture1.setDisable(true);
                btnPicture2.setDisable(true);
                btnSignature.setDisable(true);
                btnClearSignature.setDisable(true);
                btnCheckIn.setDisable(true);
            } else {
                btnReprint.setDisable(true);
            }
        } catch (Exception ex) {
            log.error("Error setting view state:", ex);
            throw ex;
        }

    }


    private WritableImage loadImageFrom(String filename) {
        if (filename != null) {
            try {
                File inputFile = new File(filename);
                BufferedImage img = ImageIO.read(inputFile);
                return SwingFXUtils.toFXImage(img, null);
            } catch (IOException e) {
                log.warn("Could not load image " + filename);
            }
        }
        return null;
    }
    private boolean captureImageTo(String filename) {
        BufferedImage image = webcamService.getImage();
        File outputFile = new File(settings.getWorkQueue() + filename);
        try {
            log.info("Writing file to " + outputFile.getAbsolutePath());
            ImageIO.write(image, "jpg", outputFile);
            return true;
        } catch (IOException ex) {
            log.error("Error writing file " + outputFile.getAbsolutePath(), ex);
        }
        return false;

    }

    public void picture1Clicked() {
        log.info("Saving picture 1");
        String filename = staff.getFilename() + "-1.jpg";
        boolean saved = captureImageTo(filename);
        if (saved) {
            staff.setPicture1Saved(true);
        }
        setViewState();
    }

    public void picture2Clicked() {
        log.info("Saving picture 2");
        String filename = staff.getFilename() + "-2.jpg";
        boolean saved = captureImageTo(filename);
        if (saved) {
            staff.setPicture2Saved(true);
        }
        setViewState();
    }

    public void saveSignatureClicked() {
        log.info("Saving signature");
        staff.setSignatureSaved(true);
        String filename = staff.getFilename() + "-3.jpg";

        BufferedImage image = sigpadService.getImage();
        File outputFile = new File(settings.getWorkQueue() + filename);

        try {
            log.info("Writing file to " + outputFile.getAbsolutePath());
            ImageIO.write(image, "jpg", outputFile);
            setViewState();
        } catch (IOException ex) {
            log.error("Error writing file " + outputFile.getAbsolutePath(), ex);
        }

    }

    public void clearSignatureClicked() {
        log.info("Clearing signature");
        sigpadService.clearSignature();
    }

    public void checkInClicked() {
        log.info("Check in Clicked");
        // Move pictures to outbound queue
        // Write checkin json in outbound queue
        staff.setCheckedIn(true);
        setViewState();
    }

    public void reprintClicked() {
        log.info("Reprint Clicked");
    }


}
