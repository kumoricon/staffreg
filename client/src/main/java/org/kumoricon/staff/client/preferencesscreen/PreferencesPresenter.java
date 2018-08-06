package org.kumoricon.staff.client.preferencesscreen;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.kumoricon.staff.client.ViewModel;
import org.kumoricon.staff.client.stafflistscreen.StafflistView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;


public class PreferencesPresenter implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(PreferencesPresenter.class);

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
        log.info("LoginView Initializing");
        viewModel.disablePreferencesMenu(true);
        viewModel.disableRefreshMenu(true);
    }


    public void saveClicked() {
        log.info("Save");
        goToStaffList();
    }

    public void cancelClicked() {
        log.info("Cancel");
        goToStaffList();
    }

    private void goToStaffList() {
        StafflistView view = new StafflistView();
        view.getViewAsync(viewModel::setMainView);
    }
}
