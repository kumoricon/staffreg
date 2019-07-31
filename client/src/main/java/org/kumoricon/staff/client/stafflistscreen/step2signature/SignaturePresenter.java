package org.kumoricon.staff.client.stafflistscreen.step2signature;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.kumoricon.staff.client.PrinterService;
import org.kumoricon.staff.client.SettingsService;
import org.kumoricon.staff.client.TransferService;
import org.kumoricon.staff.client.ViewModel;
import org.kumoricon.staff.client.model.Staff;
import org.kumoricon.staff.client.stafflistscreen.EventFactory;
import org.kumoricon.staff.client.stafflistscreen.step1photo.PhotoView;
import org.kumoricon.staff.client.stafflistscreen.step3done.DoneView;
import org.kumoricon.staff.dto.StaffEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.time.Instant;
import java.util.ResourceBundle;


public class SignaturePresenter implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(SignaturePresenter.class);

    private Staff staff;

    @FXML
    private ImageView imgSignature, imgSaved;

    @FXML
    private Button btnNext, btnBack, btnSaveSignature, btnClearSignature;

    @FXML
    private Label lblStaffName;

    @Inject
    ViewModel viewModel;

    @Inject
    EventFactory eventFactory;

    @Inject
    TransferService transferService;

    @Inject
    PrinterService printerService;

    @Inject
    SigpadService sigpadService;

    @Inject
    SettingsService settings;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewModel.disableRefreshMenu(true);
        viewModel.disablePreferencesMenu(true);

        staff = viewModel.getSelectedStaff();
        lblStaffName.setText(staff.getName());
        imgSignature.imageProperty().bind(sigpadService.getSigImageProperty());

        staff.checkedInProperty().addListener((observable, oldValue, newValue) -> {
            setViewState();
        });

        setViewState();
    }

    public void backClicked() {
        log.info("Back Clicked");
        goToPhotoView();
    }

    public void checkInClicked() {
        log.info("Check in Clicked");
        Instant checkInTime= Instant.now();
        staff.setCheckedIn(true);
        staff.setCheckedInAt(checkInTime);

        StaffEvent e = eventFactory.buildCheckInEvent(staff);
        transferService.moveImagesToOutboundDirectory(staff.getFilename());
        transferService.queueEventToSend(e);

        if (staff.isBadgePrinted()) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Badge for " + staff.getName() + " already printed", ButtonType.OK);
            alert.show();
        } else {
            staff.setBadgePrinted(true);
            printerService.tryPrintWithDialog(staff);
        }

        goToDoneView();
    }

    public void btnSaveSignatureClicked() {
        log.info("Save Signature");
        String filename = staff.getFilename() + "-2.jpg";
//        boolean saved = captureImageTo(filename);
        boolean saved = true;
        if (saved) {
            staff.setSignatureSaved(true);
        }
        setViewState();
    }

    public void btnClearSignatureClicked() {
        log.info("Clearing signature");
        sigpadService.clearSignature();
    }

    private void setImageState() {
        if (staff.isSignatureSaved() || staff.isCheckedIn()) {
//            Image image = loadImageFrom(settings.getWorkQueue() + staff.getFilename() + "-2.jpg");
//            if (image != null) {
//                imgSaved.setImage(image);
//            }
        }
    }


    private void setViewState() {
        if (staff == null) {
            log.warn("Warning: SignaturePresenter trying to set view state for null Staff");
            return;
        }
        setImageState();

        // Disable next if signature is not saved
        btnNext.setDisable(!staff.isSignatureSaved());

        // Disable signature controls if staff is checked in
        btnSaveSignature.setDisable(staff.getCheckedIn());
        btnClearSignature.setDisable(staff.getCheckedIn());

        if (staff.isSignatureSaved()) {
            btnSaveSignature.setDefaultButton(false);
            btnNext.setDefaultButton(true);
            btnNext.requestFocus();
        } else {
            btnNext.setDefaultButton(false);
            btnSaveSignature.setDefaultButton(true);
        }

    }


    private void goToPhotoView() {
        PhotoView view = new PhotoView();
        view.getViewAsync(viewModel::setMainView);
    }

    private void goToDoneView() {
        DoneView view = new DoneView();
        view.getViewAsync(viewModel::setMainView);
    }

}
