package it.disi.unitn.exceptions;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Exception class used when not enough arguments are passed to methods or constructors of this library
 */
public class NotEnoughArgumentsException extends Exception {

    /**
     * This exception's message
     */
    private final String message, itmsg;

    /**
     * The OS's default Locale
     */
    private final Locale l;

    /**
     * Class constructor which takes a message to be displayed.
     * @param message The message to be displayed
     * @param itmsg The message given to the exception translated in Italian
     */
    public NotEnoughArgumentsException(@NotNull String message, @NotNull String itmsg) {
        this.message = message;
        this.itmsg = itmsg;
        l = Locale.getDefault();
    }

    /**
     * Convenience method that allows the program to get the message to be displayed.
     * @return The message to be displayed
     */
    public String getMessage() {
        if(l == Locale.ITALIAN || l == Locale.ITALY) {
            return itmsg;
        }
        return message;
    }
}
