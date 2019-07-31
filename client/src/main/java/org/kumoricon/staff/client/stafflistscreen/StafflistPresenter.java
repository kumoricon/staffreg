package org.kumoricon.staff.client.stafflistscreen;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.kumoricon.staff.client.PrinterService;
import org.kumoricon.staff.client.TransferService;
import org.kumoricon.staff.client.ViewModel;
import org.kumoricon.staff.client.heartbeat.HeartbeatService;
import org.kumoricon.staff.client.model.Staff;
import org.kumoricon.staff.client.stafflistscreen.step1photo.PhotoView;
import org.kumoricon.staff.dto.StaffEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class StafflistPresenter implements Initializable {
    private ObservableList<Staff> staffMasterList;
    private FilteredList<Staff> staffFilteredList;
    private SortedList<Staff> staffSortedList;
    private static final Logger log = LoggerFactory.getLogger(StafflistPresenter.class);
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/uuuu");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/uuuu HH:mm:ss a z");

    @FXML
    TextField txtFilter;

    @FXML
    TableView<Staff> tblStaff;

    @FXML
    AnchorPane rightPane;

    @FXML
    Button btnCheckIn, btnReprint;

    @FXML
    TextArea txtDetails;

    @Inject
    ViewModel viewModel;

    @Inject
    PrinterService printerService;

    @Inject
    TransferService transferService;

    @Inject
    EventFactory eventFactory;

    @Inject
    StafflistService stafflistService;

    @SuppressWarnings("unused")
    @Inject
    private HeartbeatService heartbeatService;  // Just here to start initializing the heartbeat

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewModel.disableRefreshMenu(false);
        viewModel.disablePreferencesMenu(false);
        staffMasterList = stafflistService.getStaffObservableList();
        staffFilteredList  = new FilteredList<>(staffMasterList, p -> true);
        staffSortedList = new SortedList<>(staffFilteredList);

        tblStaff.setItems(staffSortedList);     // Set items here instead of in FXML because some bindings won't work
                                                // if the list doesn't exist when the view is loaded

        staffSortedList.comparatorProperty().bind(tblStaff.comparatorProperty());

        tblStaff.setOnMouseClicked(event -> staffClicked());
        // Listen for changes from keystrokes. Null checks are so updating the list in the
        // background doesn't trigger staffClicked()
        tblStaff.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && oldSelection != null && !newSelection.getUuid().equals(oldSelection.getUuid())) {
                staffClicked();
            }
        });

        btnCheckIn.setDisable(true);
        btnReprint.setDisable(true);

        txtDetails.setText("");
    }

    public void filterChanged() {
        String filterOn = txtFilter.getText().toLowerCase().trim();
        staffFilteredList.setPredicate(staff -> {
            // If filter text is empty, display all staff
            if (filterOn.isEmpty()) { return true; }

            return staff.getName().toLowerCase().contains(filterOn) ||
                    staff.getLegalName().toLowerCase().contains(filterOn) ||
                    staff.getDepartment().toLowerCase().contains(filterOn);
        });
    }

    private void staffClicked() {
        Staff selectedItem = tblStaff.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            log.info("Staff clicked: " + tblStaff.getSelectionModel().getSelectedItem());

            if (selectedItem.isCheckedIn()) {
                btnCheckIn.setDisable(true);
                btnCheckIn.setDefaultButton(false);
                btnReprint.setDisable(false);
                btnReprint.setDefaultButton(true);
            } else {
                btnCheckIn.setDisable(false);
                btnCheckIn.setDefaultButton(true);
                btnReprint.setDisable(true);
                btnReprint.setDefaultButton(false);
            }
            txtDetails.setText(buildStaffInfoString(selectedItem));
        } else {
            btnCheckIn.setDisable(true);
            btnCheckIn.setDefaultButton(false);
            btnReprint.setDisable(true);
            btnReprint.setDefaultButton(false);
            txtDetails.clear();
        }

        viewModel.setSelectedStaff(selectedItem);
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
            sb.append("Checked in at: ").append(TIME_FORMATTER.format(checkInTime));
        }

        return sb.toString();
    }

    public void clearClicked() {
        // Reset filter
        viewModel.setSelectedStaff(null);
        btnCheckIn.setDisable(true);
        btnReprint.setDisable(true);
        txtDetails.setText("");
        staffFilteredList.setPredicate(staff -> true);
        tblStaff.getSelectionModel().clearSelection();
        txtFilter.clear();
        txtFilter.requestFocus();
    }

    public void checkInClicked() {
        log.info("Check In Clicked");
        PhotoView view = new PhotoView();
        view.getViewAsync(viewModel::setMainView);
    }

    public void reprintClicked() {
        log.info("Reprint Clicked");
        Staff staff = viewModel.getSelectedStaff();
        StaffEvent e = eventFactory.buildReprintEvent(staff);
        transferService.queueEventToSend(e);
        printerService.tryPrintWithDialog(staff);
    }
}
