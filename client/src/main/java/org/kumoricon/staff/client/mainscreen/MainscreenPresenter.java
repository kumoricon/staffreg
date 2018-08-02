package org.kumoricon.staff.client.mainscreen;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import org.kumoricon.staff.client.loginscreen.LoginView;

import javax.inject.Inject;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class MainscreenPresenter implements Initializable {

    @FXML
    Label lblStatus;

    @FXML
    MenuItem menuRefresh, menuPreferences, menuQuit;

    @FXML
    AnchorPane rootPane;

    @Inject
    private String prefix;

    @Inject
    private String happyEnding;

    @Inject
    private LocalDate date;

    private String theVeryEnd;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblStatus.setText("");

        menuPreferences.setDisable(true);
        menuRefresh.setDisable(true);

        LoginView loginView = new LoginView();
        loginView.getViewAsync(rootPane.getChildren()::add);

//        this.theVeryEnd = rb.getString("theEnd");
    }

    public void quitClicked() {
        System.exit(0);
    }
}
