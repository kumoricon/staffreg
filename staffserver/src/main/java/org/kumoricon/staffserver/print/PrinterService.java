package org.kumoricon.staffserver.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kumoricon.staffserver.event.StaffEventRecord;
import org.kumoricon.staffserver.imageupload.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.Sides;
import java.awt.print.PageFormat;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrinterService {
    private static final Logger log = LoggerFactory.getLogger(PrinterService.class);

    List<String> getPrinterNames() {
        List<String> printerNames = new ArrayList<>();
        for (PrintService printer : getInstalledPrinters()) {
            printerNames.add(printer.getName());
        }
        return printerNames;
    }



    private PrintService[] getInstalledPrinters() {
        return PrintServiceLookup.lookupPrintServices(null, null);
    }


    public PrintService findByName(String printerName) {
        String printerNameToFind;
        if (printerName == null) {
            printerNameToFind = "";
        } else {
            printerNameToFind = printerName.toLowerCase();
        }
        for (PrintService printService : getInstalledPrinters()) {
            if (printerNameToFind.equals(printService.getName().toLowerCase())) {
                return printService;
            }
        }
        return null;
    }

    public void printPDFDoubleSided(PrintService printService, InputStream badgePDFStream) throws PrintException {
        try {
            DocPrintJob job = printService.createPrintJob();
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

            PrintRequestAttributeSet printRequestSet = new HashPrintRequestAttributeSet();

            // Note: Setting media size doesn't seem to override printer settings, the defaults have
            // to be set properly in CUPS as well.
            printRequestSet.add(Sides.DUPLEX);
            printRequestSet.add(MediaSizeName.INVOICE);

            DocAttributeSet docAttributeSet = new HashDocAttributeSet();
            docAttributeSet.add(MediaSizeName.INVOICE);
            docAttributeSet.add(Sides.DUPLEX);


            Doc doc = new SimpleDoc(badgePDFStream, flavor, docAttributeSet);
            job.print(doc, printRequestSet);
        } finally {
            try {
                badgePDFStream.close();
            } catch (IOException ignored) {

            }
        }

    }
}
