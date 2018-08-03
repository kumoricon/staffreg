package org.kumoricon.staff.client.mainscreen;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import org.kumoricon.staff.client.ViewModel;
import org.kumoricon.staff.client.loginscreen.LoginView;
import org.kumoricon.staff.client.preferencesscreen.PreferencesView;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;


public class MainscreenPresenter implements Initializable {

    @FXML
    Label lblStatus;

    @FXML
    MenuItem menuRefresh, menuPreferences, menuQuit;

    @FXML
    BorderPane rootPane;

    @Inject
    private ViewModel viewModel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblStatus.setText("");
        rootPane.centerProperty().bind(viewModel.mainViewProperty());

        menuPreferences.disableProperty().bind(viewModel.preferencesMenuDisabledProperty());
        menuRefresh.disableProperty().bind(viewModel.refreshMenuDisabledProperty());
        goToLogin();


//        this.theVeryEnd = rb.getString("theEnd");
    }

    private void goToLogin() {
        LoginView loginView = new LoginView();
        loginView.getViewAsync(viewModel::setMainView);
    }

    public void goToPreferences() {
        PreferencesView view = new PreferencesView();
        view.getViewAsync(viewModel::setMainView);
    }

    public void quitClicked() {
        System.exit(0);
    }
}
