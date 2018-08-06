package org.kumoricon.staff.client.stafflistscreen.checkindetails;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WebcamService {
    private Webcam selWebCam;
    private boolean stopCamera = true;
    private BufferedImage grabbedImage;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
    private static final Logger log = LoggerFactory.getLogger(WebcamService.class);

    public WebcamService() {
        initializeWebCam(0);
    }

    private void initializeWebCam(final int webCamIndex) {
        Task<Void> webCamInitializer = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
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
                        Platform.runLater(new Runnable() {
                            public void run() {
                                final Image mainiamge = SwingFXUtils
                                        .toFXImage(grabbedImage, null);
                                imageProperty.set(mainiamge);
                            }
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

    public BufferedImage getImage() {
        if (selWebCam != null) {
            return selWebCam.getImage();
        }
        return null;
    }

    public void closeCamera() {
        stopCamera = true;
        log.info("Closing camera");
        if (selWebCam != null) {
            selWebCam.close();
        }
    }

    public ObjectProperty<Image> getImageProperty() {
        return imageProperty;
    }


}
