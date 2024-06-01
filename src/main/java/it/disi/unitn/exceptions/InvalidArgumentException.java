package it.disi.unitn.exceptions;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * This class represents an exception which is risen when an invalid argument is given to a method or constructor.
 */
public class InvalidArgumentException extends GeneralException {

    /**
     * Class constructor.
     * @param message The message given to the exception.
     * @param itmsg The message given to the exception translated in Italian
     */
    public InvalidArgumentException(@NotNull String message, @NotNull String itmsg) {
        super(message, itmsg);
    }

    @Override
    public String getMessage() {
        if(l == Locale.ITALIAN || l == Locale.ITALY) {
            return itmsg;
        }
        return message;
    }
}
