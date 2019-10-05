package org.kumoricon.staffserver.controlleradvice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * This class gets information from various cookies and injects it in to the Thymeleaf model
 * class, so they can be used directly in all templates.
 */
@ControllerAdvice
public class CookieControllerAdvice {
    public static final String PRINTER_COOKIE_NAME = "PRINTERNAME";

    /**
     * Provides the currently selected printer name, or null if the cookie is missing or malformed.
     * @param request
     * @return
     */
    @ModelAttribute("selectedPrinter")
    public String selectedPrinter(final HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie c : request.getCookies()) {
                if (c.getName().equals(PRINTER_COOKIE_NAME)) {
                    // This has a try/catch around it so that the UI isn't broken if any weird values
                    // end up in the cookie and it fails to parse.
                    try {
                        PrinterSettings settings = PrinterSettings.fromCookieValue(c.getValue());
                        return settings.getPrinterName();
                    } catch (Exception ex) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}