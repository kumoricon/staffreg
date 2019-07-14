package org.kumoricon.staff.client.stafflistscreen.checkindetails;

//import com.topaz.sigplus.SigPlus;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.beans.Beans;
import java.io.IOException;

public class SigpadService {
//    private SigPlus sigPlus;
    private static final Logger log = LoggerFactory.getLogger(SigpadService.class);
    private boolean stopSignaturePad;
    private BufferedImage signatureGrabbedImage;
    private ObjectProperty<Image> sigImageProperty = new SimpleObjectProperty<>();

    public SigpadService() {
        initializeSignaturePad();
    }

    private void initializeSignaturePad() {
        Task<Void> sigPadIntilizer = new Task<Void>() {
            @Override
            protected Void call() {
                log.info("Initializing signature pad");
//                ClassLoader cl = (com.topaz.sigplus.SigPlus.class).getClassLoader();
                log.info("Sigpad Path: " + System.getProperty("java.library.path"));
//                try {
//                    sigPlus = (SigPlus) Beans.instantiate( cl, "com.topaz.sigplus.SigPlus" );
//
//                    sigPlus.setTabletModel( "SignatureGemLCD1X5" );
//                    sigPlus.setTabletComPort( "HID1" );
//
//                    sigPlus.setTabletState(1);
//                    sigPlus.setImageXSize(640);
//                    sigPlus.setImageYSize(128);
//                    startSignatureStream();   // Just to add an empty box to the UI; should only be collecting the
//                } catch (UnsatisfiedLinkError | NoClassDefFoundError | ClassNotFoundException | IOException ex) {
//                    log.error("Error initializing signature pad", ex);
//                }

                return null;
            }
        };

        Thread th = new Thread(sigPadIntilizer);
        th.setDaemon(true);
        th.setUncaughtExceptionHandler((t1, e) -> log.error("Sigpad error:", e));
        th.start();
    }


    protected void stopSignatureStream() {
        log.debug("Stopping signature pad stream");
        stopSignaturePad = true;
    }

    protected void startSignatureStream() {
        log.debug("Starting signature Stream");
        stopSignaturePad = false;
//        sigPlus.clearTablet();
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                while (!stopSignaturePad) {
                    try {
                        Thread.sleep(100);
//                        if ((signatureGrabbedImage = sigPlus.sigImage()) != null) {
//                            Platform.runLater(new Runnable() {
//                                public void run() {
//                                    final Image sigImage = SwingFXUtils
//                                            .toFXImage(signatureGrabbedImage, null);
//                                    sigImageProperty.set(sigImage);
//                                }
//                            });
//                            signatureGrabbedImage.flush();
//                        }
                    } catch (Exception e) {
                        log.error("Error streaming from signature pad", e);
                    }
                }

                return null;
            }

        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.setUncaughtExceptionHandler((t, e) -> {log.error("Error in startSignatureStream:", e);});
        th.start();
    }

    public ObjectProperty<Image> getSigImageProperty() {
        return sigImageProperty;
    }

    public void clearSignature() {
//        sigPlus.clearTablet();
    }

    public BufferedImage getImage() {
        return signatureGrabbedImage;
    }
}
