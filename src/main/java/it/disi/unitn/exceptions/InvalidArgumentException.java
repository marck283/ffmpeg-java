package it.disi.unitn.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * This class represents an exception which is risen when an invalid argument is given to a method or constructor.
 */
public class InvalidArgumentException extends Exception {

    /**
     * The message that will be printed by this instance of InvalidArgumentException.
     */
    private String message = "";

    /**
     * Class constructor.
     * @param message The message given to the exception.
     */
    public InvalidArgumentException(@NotNull String message) {
        this.message = message;
    }

    /**
     * This method returns the message that was given to the constructor.
     * @return The message that was given to the constructor
     */
    public String getMessage() {
        return message;
    }
}
