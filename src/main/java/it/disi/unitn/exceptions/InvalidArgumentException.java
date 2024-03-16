package it.disi.unitn.exceptions;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * This class represents an exception which is risen when an invalid argument is given to a method or constructor.
 */
public class InvalidArgumentException extends Exception {

    /**
     * The message that will be printed by this instance of InvalidArgumentException.
     */
    private final String message, itmsg;

    /**
     * Class constructor.
     * @param message The message given to the exception.
     * @param itmsg The message given to the exception translated in Italian
     */
    public InvalidArgumentException(@NotNull String message, @NotNull String itmsg) {
        this.message = message;
        this.itmsg = itmsg;
    }

    /**
     * This method returns the message that was given to the constructor.
     * @return The message that was given to the constructor
     */
    public String getMessage() {
        Locale l = Locale.getDefault();
        if(l == Locale.ITALIAN || l == Locale.ITALY) {
            return itmsg;
        }
        return message;
    }
}
