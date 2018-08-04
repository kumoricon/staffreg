package org.kumoricon.staff.client.stafflistscreen.checkindetails;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.kumoricon.staff.client.ViewModel;
import org.kumoricon.staff.client.model.Staff;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;


public class CheckinDetailsPresenter implements Initializable {
    private Staff staff;

    @FXML
    ImageView imgWebcam, imgPicture1, imgPicture2, imgSignature;

    @FXML
    Button btnPicture1, btnPicture2, btnSignature, btnClearSignature, btnCheckIn, btnReprint;
    @Inject
    ViewModel viewModel;

    @Inject
    CheckinService stafflistService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        staff = viewModel.getSelectedStaff();

        setViewState();
    }

    private void setViewState() {
        try {
            if (staff.isPicture1Saved()) {
                imgPicture1.setImage(new Image("/picture1saved.png"));
            } else {
                imgPicture1.setImage(new Image("/picture1example.png"));
            }

            if (staff.isPicture2Saved()) {
                imgPicture2.setImage(new Image("/picture2saved.png"));
            } else {
                imgPicture2.setImage(new Image("/picture2example.png"));
            }

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
            System.out.println(ex);
        }

    }


    public void picture1Clicked() {
        System.out.println("Saving picture 1");
        staff.setPicture1Saved(true);
        setViewState();
    }

    public void picture2Clicked() {
        System.out.println("Saving picture 2");
        staff.setPicture2Saved(true);
        setViewState();
    }

    public void saveSignatureClicked() {
        System.out.println("Saving signature");
        staff.setSignatureSaved(true);
        setViewState();
    }

    public void clearSignatureClicked() {
        System.out.println("Clearing signature");
    }

    public void checkInClicked() {
        System.out.println("Check in Clicked");
        staff.setCheckedIn(true);
        setViewState();
    }

    public void reprintClicked() {
        System.out.println("Reprint Clicked");
    }


}
