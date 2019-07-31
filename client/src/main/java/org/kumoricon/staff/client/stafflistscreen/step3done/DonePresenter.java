package org.kumoricon.staff.client.stafflistscreen.step3done;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.kumoricon.staff.client.SettingsService;
import org.kumoricon.staff.client.ViewModel;
import org.kumoricon.staff.client.model.Staff;
import org.kumoricon.staff.client.stafflistscreen.StafflistView;
import org.kumoricon.staff.client.stafflistscreen.step2signature.SignatureView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class DonePresenter implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(DonePresenter.class);
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/uuuu");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/uuuu HH:mm:ss a z");

    private Staff staff;

    @FXML
    private Button btnBack, btnDone;

    @FXML
    private Label lblStaffName;

    @FXML
    private TextArea txtStaff;

    @Inject
    ViewModel viewModel;

    @Inject
    SettingsService settings;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewModel.disableRefreshMenu(true);
        viewModel.disablePreferencesMenu(true);

        staff = viewModel.getSelectedStaff();
        lblStaffName.setText(staff.getName());

        staff.checkedInProperty().addListener((observable, oldValue, newValue) -> setViewState());

        setViewState();
    }

    public void backClicked() {
        log.info("Back Clicked");
        goToSignatureView();
    }

    public void btnDoneClicked() {
        log.info("Done");
        goToStaffListView();
    }

    public void btnClearSignatureClicked() {

    }

    private static String buildStaffInfoString(Staff staff) {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(staff.getName()).append("\n\n");
        sb.append("Legal Name: ").append(staff.getLegalName()).append("\n");
        sb.append("Birth date: ").append(DAY_FORMATTER.format(staff.getBirthDate()))
                .append(" (").append(staff.getCurrentAge()).append(")\n\n");
        sb.append("Department: ").append(staff.getDepartment()).append("\n");
        sb.append("T-Shirt size: ").append(staff.getShirtSize()).append("\n\n");

        if (staff.isCheckedIn()) {
            ZonedDateTime checkInTime = staff.getCheckedInAt().atZone(ZoneId.systemDefault());
            sb.append("Checked in at: ").append(TIME_FORMATTER.format(checkInTime)).append("\n");
            sb.append("Photo saved: ").append(staff.isPicture1Saved()).append("\n");
            sb.append("Signature saved: ").append(staff.isSignatureSaved()).append("\n");
        }

        return sb.toString();
    }




    private void setViewState() {
        if (staff == null) {
            log.warn("Warning: DonePresenter trying to set view state for null Staff");
            return;
        }

        // Disable signature controls if staff is checked in
        if (staff.isCheckedIn()) {
            btnDone.setDisable(false);
            btnDone.setDefaultButton(true);
        } else {
            btnDone.setDisable(true);
            btnDone.setDefaultButton(false);
        }

        txtStaff.setText(buildStaffInfoString(staff));
    }





    private void goToSignatureView() {
        SignatureView view = new SignatureView();
        view.getViewAsync(viewModel::setMainView);
    }

    private void goToStaffListView() {
        StafflistView view = new StafflistView();
        view.getViewAsync(viewModel::setMainView);
    }

}
