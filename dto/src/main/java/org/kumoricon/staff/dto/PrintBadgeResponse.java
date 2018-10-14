package org.kumoricon.staff.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PrintBadgeResponse {
    private String printedToPrinterName;
    private String staffId;
    private String statusMessage;
    private Boolean success;

    @JsonCreator
    public PrintBadgeResponse(@JsonProperty(value = "printedToPrinterName", required = true) String printedToPrinterName,
                              @JsonProperty(value = "staffId", required = true) String staffId,
                              @JsonProperty(value = "statusMessage", required = true, defaultValue = "") String statusMessage,
                              @JsonProperty(value = "success", required = true) Boolean success) {
        this.printedToPrinterName = printedToPrinterName;
        this.staffId = staffId;
        this.statusMessage = statusMessage;
        this.success = success;
    }

    public String getPrintedToPrinterName() {
        return printedToPrinterName;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public Boolean getSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "PrintBadgeResponse{" +
                "printedToPrinterName='" + printedToPrinterName + '\'' +
                ", staffId='" + staffId + '\'' +
                ", statusMessage='" + statusMessage + '\'' +
                ", success=" + success +
                '}';
    }
}
