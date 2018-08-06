package org.kumoricon.staff.client.stafflistscreen;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.kumoricon.staff.client.ViewModel;
import org.kumoricon.staff.client.model.Staff;
import org.kumoricon.staff.client.stafflistscreen.checkindetails.CheckinDetailsView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;


public class StafflistPresenter implements Initializable {
    private ObservableList<Staff> staffMasterList = FXCollections.observableArrayList();
    private FilteredList<Staff> staffFilteredList = new FilteredList<>(staffMasterList, p -> true);
    private SortedList<Staff> staffSortedList = new SortedList<>(staffFilteredList);
    private static final Logger log = LoggerFactory.getLogger(StafflistPresenter.class);

    @FXML
    TextField txtFilter;

    @FXML
    TableView<Staff> tblStaff;

    @FXML
    AnchorPane rightPane;

    @Inject
    ViewModel viewModel;

    @Inject
    StafflistService stafflistService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewModel.disableRefreshMenu(false);
        viewModel.disablePreferencesMenu(false);

        staffSortedList.comparatorProperty().bind(tblStaff.comparatorProperty());

        staffMasterList.add(new Staff("Some", "Dude","Department of Awesome", "L"));
        staffMasterList.add(new Staff("Other", "Guy", "Department of Things", "M"));
        staffMasterList.add(new Staff("Alice", "Anderson", "Party People", "S"));

        for (int i = 1; i <= 5000; i++) {
            staffMasterList.add(new Staff("Guy"+i, "Congoer", "Registration", "XL"));
        }
    }

    public void filterChanged() {
        String filterOn = txtFilter.getText().toLowerCase().trim();
        staffFilteredList.setPredicate(staff -> {
            // If filter text is empty, display all staff
            if (filterOn.isEmpty()) { return true; }

            return staff.getFirstName().toLowerCase().contains(filterOn) ||
                    staff.getLastName().toLowerCase().contains(filterOn) ||
                    staff.getDepartment().toLowerCase().contains(filterOn);
        });
    }

    public void staffClicked() {
        Staff selectedItem = tblStaff.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            log.info("Staff clicked: " + tblStaff.getSelectionModel().getSelectedItem());

        }
        viewModel.setSelectedStaff(selectedItem);
        CheckinDetailsView details = new CheckinDetailsView();
        details.getViewAsync(rightPane.getChildren()::setAll);
    }

    public void clearClicked() {
        // Reset filter
        viewModel.setSelectedStaff(null);
        rightPane.getChildren().removeAll(rightPane.getChildren());
        staffFilteredList.setPredicate(staff -> true);
        tblStaff.getSelectionModel().clearSelection();
        txtFilter.clear();
        txtFilter.requestFocus();
    }

    public ObservableList<Staff> getStaffSortedList() {
        return staffSortedList;
    }
}
