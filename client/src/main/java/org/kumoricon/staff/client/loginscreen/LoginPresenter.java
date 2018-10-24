package org.kumoricon.staff.client.loginscreen;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import org.kumoricon.staff.client.SettingsService;
import org.kumoricon.staff.client.ViewModel;
import org.kumoricon.staff.client.stafflistscreen.StafflistView;
import org.kumoricon.staff.dto.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.inject.Inject;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;


public class LoginPresenter implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(LoginPresenter.class);

    @FXML
    TextField txtUsername, txtPassword, txtServerURL;

    @FXML
    Button btnLogin, btnCancel;

    @Inject
    LoginService loginService;

    @Inject
    SettingsService settingsService;

    @Inject
    ViewModel viewModel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("LoginView Initializing");
        viewModel.disablePreferencesMenu(true);
        viewModel.disableRefreshMenu(true);

        txtServerURL.textProperty().setValue(settingsService.getHostUrl());
    }



    public void loginClicked() {
        try {
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            String serverUrl = txtServerURL.getText();

            settingsService.setHostUrl(serverUrl);
            settingsService.saveSettings();

            LoginResponse response = loginService.login(username, password, serverUrl);
            if (response.isPasswordChangeRequired()) {
                showNewPasswordWindow();
            } else {
                if (response.isSuccess()) {
                    goToStaffListView();
                }
            }
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeaderText("Error logging in");
            alert.setContentText(ex.getMessage());
            alert.show();
        }
    }


    private void goToStaffListView() {
        viewModel.disablePreferencesMenu(false);
        viewModel.disableRefreshMenu(false);
        StafflistView view = new StafflistView();
        view.getViewAsync(viewModel::setMainView);
    }

    private void showNewPasswordWindow() {
        PasswordInputDialog dialog = new PasswordInputDialog();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Password must be 4+ characters");
        dialog.setContentText("Password:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> {
            log.info("New password set");
            loginService.setNewPassword(s);
            goToStaffListView();
        });
    }

    public void quitClicked() {
        System.exit(0);
    }

}
