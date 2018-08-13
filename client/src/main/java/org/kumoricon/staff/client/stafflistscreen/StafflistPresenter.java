package org.kumoricon.staff.client.stafflistscreen;

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
    private ObservableList<Staff> staffMasterList;
    private FilteredList<Staff> staffFilteredList;
    private SortedList<Staff> staffSortedList;
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
        staffMasterList = stafflistService.getStaffObservableList();
        staffFilteredList  = new FilteredList<>(staffMasterList, p -> true);
        staffSortedList = new SortedList<>(staffFilteredList);

        tblStaff.setItems(staffSortedList);     // Set items here instead of in FXML because some bindings won't work
                                                // if the list doesn't exist when the view is loaded

        staffSortedList.comparatorProperty().bind(tblStaff.comparatorProperty());

        tblStaff.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                staffClicked();
            }
        });

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
