package org.kumoricon.staff.client.stafflist;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.kumoricon.staff.client.ViewModel;
import org.kumoricon.staff.client.model.Staff;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;


public class StafflistPresenter implements Initializable {

    @FXML
    TextField txtFilter;

    @FXML
    TableView<Staff> staffTableView;

    @Inject
    ViewModel viewModel;

    @Inject
    StafflistService stafflistService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewModel.disableRefreshMenu(false);
        viewModel.disablePreferencesMenu(false);
        System.out.println("StafflistView Initializing");
    }

    public void filterChanged() {
        System.out.println(txtFilter.getText());
    }

    public void staffClicked() {
        System.out.println("Staff clicked");
    }
}
