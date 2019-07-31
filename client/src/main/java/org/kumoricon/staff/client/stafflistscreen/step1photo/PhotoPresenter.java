package org.kumoricon.staff.client.stafflistscreen.step1photo;

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
import org.kumoricon.staff.client.model.Staff;
import org.kumoricon.staff.client.stafflistscreen.StafflistView;
import org.kumoricon.staff.client.stafflistscreen.step2signature.SignatureView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PhotoPresenter implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(PhotoPresenter.class);

    private Staff staff;

    @FXML
    private ImageView imgWebcam, imgSaved;

    @FXML
    private Button btnNext, btnBack, btnSavePhoto;

    @FXML
    private Label lblStaffName;

    @Inject
    ViewModel viewModel;

    @Inject
    WebcamService webcamService;

    @Inject
    SettingsService settings;


    @Inject
    PhotoService photoService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewModel.disableRefreshMenu(true);
        viewModel.disablePreferencesMenu(true);

        imgWebcam.imageProperty().bind(webcamService.getImageProperty());

        staff = viewModel.getSelectedStaff();

        lblStaffName.setText(staff.getName());

        staff.checkedInProperty().addListener((observable, oldValue, newValue) -> setViewState());

        setViewState();
    }

    public void backClicked() {
        log.info("Back Clicked");
        goToStaffListView();
    }

    public void nextClicked() {
        log.info("Next Clicked");

        goToSignatureView();
    }

    public void btnSavePhotoClicked() {
        log.info("Save Photo");
        String filename = staff.getFilename() + "-1.jpg";
        boolean saved = captureImageTo(filename);
        if (saved) {
            staff.setPicture1Saved(true);
        }
        setViewState();
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


    private void setImageState() {
        if (staff.isPicture1Saved() || staff.isCheckedIn()) {
            Image image = loadImageFrom(settings.getWorkQueue() + staff.getFilename() + "-1.jpg");
            if (image == null) {
                image = new Image("/images/picture1saved.png");
            }
            imgSaved.setImage(image);

        } else {
            imgSaved.setImage(new Image("/images/picture1example.png"));
        }
    }


    private void setViewState() {
        if (staff == null) {
            log.warn("Warning: PhotoPresenter trying to set view state for null Staff");
            return;
        }
        setImageState();

        // Disable next if picture isn't saved
        btnNext.setDisable(!staff.isPicture1Saved());

        // Disable photo controls if staff is checked in
        btnSavePhoto.setDisable(staff.getCheckedIn());

        if (staff.isPicture1Saved()) {
            btnSavePhoto.setDefaultButton(false);
            btnNext.setDefaultButton(true);
            btnNext.requestFocus();
        } else {
            btnSavePhoto.setDefaultButton(true);
        }
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


    private void goToStaffListView() {
        StafflistView view = new StafflistView();
        view.getViewAsync(viewModel::setMainView);
    }

    private void goToSignatureView() {
        SignatureView view = new SignatureView();
        view.getViewAsync(viewModel::setMainView);
    }

}
