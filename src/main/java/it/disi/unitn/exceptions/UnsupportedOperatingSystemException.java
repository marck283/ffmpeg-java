package it.disi.unitn.exceptions;

import java.util.Locale;

/**
 * This exception is thrown when the operating system on which the user is operating is not supported. Currently
 * supported Operating Systems include only Windows and Linux (e.g., Kali Linux, Ubuntu).
 */
public class UnsupportedOperatingSystemException extends GeneralException {

    /**
     * The class's constructor.
     */
    public UnsupportedOperatingSystemException() {
        super("Unsupported operating system", "Sistema operativo non supportato");
    }

    @Override
    public String getMessage() {
        if(l == Locale.ITALIAN || l == Locale.ITALY) {
            return itmsg;
        }
        return message;
    }
}
