package org.kumoricon.staff.client.loginscreen;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginPresenter implements Initializable {

    @FXML
    TextField txtUsername, txtPassword, txtServerURL;

    @FXML
    Button btnLogin, btnCancel;

    @Inject
    LoginService loginService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("LoginView Initializing");
    }


    public void loginClicked() {
        loginService.login(txtUsername.getText(), txtPassword.getText(), txtServerURL.getText());
        System.out.println("Login");
    }

    public void quitClicked() {
        System.exit(0);
    }
}
