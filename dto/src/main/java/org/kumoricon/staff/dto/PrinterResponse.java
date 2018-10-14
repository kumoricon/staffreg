package org.kumoricon.staff.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PrinterResponse {
    private String name;

    @JsonCreator
    public PrinterResponse(@JsonProperty(value = "name", required = true) String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "PrinterResponse{" +
                "name='" + name + '\'' +
                '}';
    }
}
