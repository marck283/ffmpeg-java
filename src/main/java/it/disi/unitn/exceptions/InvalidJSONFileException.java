package it.disi.unitn.exceptions;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * This exception provides a way to tell the user that the given JSON file is not valid.
 */
public class InvalidJSONFileException extends GeneralException {

    /**
     * This exception's constructor.
     * @param msg The English message. This value cannot be null or an empty string.
     * @param itmsg The Italian message. This value cannot be null or an empty string.
     */
    public InvalidJSONFileException(@NotNull String msg, @NotNull String itmsg) {
        super(msg, itmsg);
    }

    @Override
    public String getMessage() {
        if(l == Locale.ITALIAN || l == Locale.ITALY) {
            return itmsg;
        }
        return message;
    }

}
