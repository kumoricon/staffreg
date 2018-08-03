package org.kumoricon.staff.client.preferencesscreen;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.kumoricon.staff.client.ViewModel;
import org.kumoricon.staff.client.stafflist.StafflistView;


import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;


public class PreferencesPresenter implements Initializable {
    @FXML
    TextField txtUsername;

    @FXML
    Button btnSave, btnCancel;

    @Inject
    PreferencesService preferencesService;

    @Inject
    ViewModel viewModel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("LoginView Initializing");
        viewModel.disablePreferencesMenu(true);
        viewModel.disableRefreshMenu(true);
    }


    public void saveClicked() {
        System.out.println("Save");
        goToStaffList();
    }

    public void cancelClicked() {
        System.out.println("Cancel");
        goToStaffList();
    }

    private void goToStaffList() {
        StafflistView view = new StafflistView();
        view.getViewAsync(viewModel::setMainView);
    }
}
