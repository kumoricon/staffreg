package org.kumoricon.staff.client.preferencesscreen;

import com.github.sarxos.webcam.Webcam;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.kumoricon.staff.client.SettingsService;
import org.kumoricon.staff.client.ViewModel;
import org.kumoricon.staff.client.stafflistscreen.StafflistView;
import org.kumoricon.staff.client.PrinterService;
import org.kumoricon.staff.client.stafflistscreen.step1photo.WebcamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;


public class PreferencesPresenter implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(PreferencesPresenter.class);

    @FXML
    ComboBox<Webcam> cmbWebcam;

    @FXML
    ComboBox<String> cmbPrinter;

    @FXML
    Button btnSave, btnCancel, btnRefreshPrinters;

    @Inject
    SettingsService settingsService;

    @Inject
    WebcamService webcamService;

    @Inject
    PrinterService printerService;

    @Inject
    ViewModel viewModel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("LoginView Initializing");
        viewModel.disablePreferencesMenu(true);
        viewModel.disableRefreshMenu(true);

        cmbWebcam.setItems(webcamService.getAvailableWebcams());
        cmbPrinter.setItems(printerService.getAvailablePrinters());

        cmbWebcam.getSelectionModel().select(settingsService.getWebcamId());
        cmbPrinter.getSelectionModel().select(settingsService.getPrinterName());
    }


    public void saveClicked() {
        log.info("Save");
        settingsService.setWebcamId(cmbWebcam.getSelectionModel().getSelectedIndex());
        settingsService.setPrinterName(cmbPrinter.getSelectionModel().getSelectedItem());
        settingsService.saveSettings();
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

    public void refreshPrintersClicked() {
        printerService.refreshPrinterList();
    }

    public void webcamSelected(ActionEvent actionEvent) {
        settingsService.setWebcamId(cmbWebcam.getSelectionModel().getSelectedIndex());
    }

    public void printerSelected(ActionEvent actionEvent) {
        settingsService.setPrinterName(cmbPrinter.getSelectionModel().getSelectedItem());
    }

}
