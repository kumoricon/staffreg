package org.kumoricon.staff.client.mainscreen;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import org.kumoricon.staff.client.BadgeImageDownloadService;
import org.kumoricon.staff.client.HealthService;
import org.kumoricon.staff.client.ViewModel;
import org.kumoricon.staff.client.heartbeat.HeartbeatService;
import org.kumoricon.staff.client.loginscreen.LoginView;
import org.kumoricon.staff.client.preferencesscreen.PreferencesView;
import org.kumoricon.staff.client.stafflistscreen.step1photo.WebcamService;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;


public class MainscreenPresenter implements Initializable {

    @FXML
    Label lblStatus, lblHeartbeat;

    @FXML
    MenuItem menuRefresh, menuPreferences, menuQuit;

    @FXML
    BorderPane rootPane;

    @Inject
    private ViewModel viewModel;

    @Inject
    private HealthService healthServiceService;

    @Inject
    HeartbeatService heartbeatService;

    @SuppressWarnings("unused")
    @Inject
    private WebcamService webcamService;        // Just here to start initializing the webcam as early as possible

    @SuppressWarnings(value = "unused")
    @Inject
    private BadgeImageDownloadService badgeImageDownloadService; // Here to start background service

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblStatus.setText("");
        lblHeartbeat.setText("");
        rootPane.centerProperty().bind(viewModel.mainViewProperty());
        menuPreferences.disableProperty().bind(viewModel.preferencesMenuDisabledProperty());
        menuRefresh.disableProperty().bind(viewModel.refreshMenuDisabledProperty());

        lblStatus.textProperty().bind(healthServiceService.statusMessageProperty());
        lblHeartbeat.textProperty().bind(heartbeatService.statusMessageProperty());
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
