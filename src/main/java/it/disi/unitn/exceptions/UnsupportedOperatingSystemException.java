package it.disi.unitn.exceptions;

import java.util.Locale;

/**
 * This exception is thrown when the operating system on which the user is operating is not supported. Currently
 * supported Operating Systems include only Windows and Linux (e.g., Kali Linux, Ubuntu) operating systems.
 */
public class UnsupportedOperatingSystemException extends Exception {
    /**
     * This exception's message.
     */
    private final String message;

    /**
     * The class's constructor.
     */
    public UnsupportedOperatingSystemException() {
        Locale locale = Locale.getDefault();
        if(locale == Locale.ITALIAN || locale == Locale.ITALY) {
            message = "Sistema operativo non supportato";
        } else {
            message = "Unsupported operating system";
        }
    }

    public String getMessage() {
        return message;
    }
}
