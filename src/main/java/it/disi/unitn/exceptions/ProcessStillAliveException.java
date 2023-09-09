package it.disi.unitn.exceptions;

import java.util.Locale;

/**
 * This exception is thrown when the child process is still alive under certain conditions. This exception should not be
 * used for any purpose other than generating pictures with a neural network.
 */
public class ProcessStillAliveException extends Exception {

    /**
     * This exception's message
     */
    private final String message;

    /**
     * Class's constructor
     */
    public ProcessStillAliveException() {
        Locale locale = Locale.getDefault();
        if(locale == Locale.ITALY || locale == Locale.ITALIAN) {
            message = "Il processo figlio e' ancora attivo.";
        } else {
            message = "The child process is still alive.";
        }
    }

    /**
     * This method is a convenience method that returns this exception's message.
     * @return This exception's message
     */
    public String getMessage() {
        return message;
    }
}
