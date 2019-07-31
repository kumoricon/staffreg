package org.kumoricon.staff.client.stafflistscreen.step1photo;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.kumoricon.staff.client.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class WebcamService {
    private Webcam selWebCam;
    private boolean stopCamera = true;
    private BufferedImage grabbedImage;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
    private ObservableList<Webcam> availableWebcams = FXCollections.observableArrayList();
    private static final Logger log = LoggerFactory.getLogger(WebcamService.class);

    @Inject
    private SettingsService settingsService;

    public WebcamService() {
        initializeWebCam(0);
    }

    @PostConstruct
    private void init() {
        getWebcamList();
        settingsService.getWebcamProperty().addListener((observable, oldValue, newValue) -> initializeWebCam((Integer) newValue));
    }

    private void getWebcamList() {
        Task<Void> getWebcamList = new Task<Void>() {
            @Override
            protected Void call() {
                log.info("Searching for webcams...");
                List<Webcam> webcams = Webcam.getWebcams();
                availableWebcams.setAll(webcams);
                log.info("Webcams: " + availableWebcams);
                return null;
            }
        };

        Thread t = new Thread(getWebcamList);
        t.setDaemon(true);
        t.start();
    }

    private void initializeWebCam(final int webCamIndex) {
        Task<Void> webCamInitializer = new Task<Void>() {
            @Override
            protected Void call() {
            log.info("initializeWebcam " + webCamIndex);
            if (selWebCam != null) {
                closeCamera();
            }

            selWebCam = Webcam.getWebcams().get(webCamIndex);
            selWebCam.setViewSize(new Dimension(640, 480));
            selWebCam.open();
            startWebCamStream();
            return null;
            }

        };

        Thread t = new Thread(webCamInitializer);
        t.setDaemon(true);
        t.setUncaughtExceptionHandler((t1, e) -> log.error("Error initializing webcam", e));
        t.start();
    }

    private void startWebCamStream() {
        stopCamera = false;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                log.info("webcam stream running");
                while (!stopCamera) {
                    if ((grabbedImage = selWebCam.getImage()) != null) {
                        Platform.runLater(() -> {
                            final Image mainiamge = SwingFXUtils
                                    .toFXImage(grabbedImage, null);
                            imageProperty.set(mainiamge);
                        });
                        grabbedImage.flush();
                    }
                    Thread.sleep(80);
                }
                return null;
            }
        };
//        scheduler.schedule(task, 1, TimeUnit.SECONDS);

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.setUncaughtExceptionHandler((t1, e) -> log.error("Error in startWebCamStream", e));
        th.start();
    }

    BufferedImage getImage() {
        if (selWebCam != null) {
            return selWebCam.getImage();
        }
        return null;
    }

    private void closeCamera() {
        stopCamera = true;
        log.info("Closing camera");
        if (selWebCam != null) {
            selWebCam.close();
        }
    }

    public ObservableList<Webcam> getAvailableWebcams() {
        return availableWebcams;
    }

    public ObjectProperty<Image> getImageProperty() {
        return imageProperty;
    }


}
