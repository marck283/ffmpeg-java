package it.disi.unitn.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * Exception class used when not enough arguments are passed to methods or constructors of this library
 */
public class NotEnoughArgumentsException extends Exception {

    /**
     * This exception's message
     */
    private final String message;

    /**
     * Class constructor which takes a message to be displayed.
     * @param message The message to be displayed
     */
    public NotEnoughArgumentsException(@NotNull String message) {
        this.message = message;
    }

    /**
     * Convenience method that allows the program to get the message to be displayed.
     * @return The message to be displayed
     */
    public String getMessage() {
        return message;
    }
}
