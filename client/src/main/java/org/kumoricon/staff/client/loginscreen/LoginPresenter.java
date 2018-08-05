package org.kumoricon.staff.client.loginscreen;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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


public class LoginPresenter implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(LoginPresenter.class);
    private final BooleanProperty loggedIn = new SimpleBooleanProperty();

    @FXML
    TextField txtUsername, txtPassword, txtServerURL;

    @FXML
    Button btnLogin, btnCancel;

    @Inject
    LoginService loginService;

    @Inject
    ViewModel viewModel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("LoginView Initializing");
        viewModel.disablePreferencesMenu(true);
        viewModel.disableRefreshMenu(true);
        loggedIn.set(false);
    }


    public void loginClicked() {
        if (loginService.login(txtUsername.getText(), txtPassword.getText(), txtServerURL.getText())) {
            viewModel.disablePreferencesMenu(false);
            StafflistView view = new StafflistView();
            view.getViewAsync(viewModel::setMainView);
        }


    }

    public void quitClicked() {
        System.exit(0);
    }

    public boolean isLoggedIn() { return loggedIn.get(); }
    public BooleanProperty loggedInProperty() { return loggedIn; }
}
