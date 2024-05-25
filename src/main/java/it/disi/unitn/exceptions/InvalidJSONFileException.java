package it.disi.unitn.exceptions;

import it.disi.unitn.StringExt;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * This exception provides a way to tell the user that the given JSON file is not valid.
 */
public class InvalidJSONFileException extends Exception {

    /**
     * The message that will be printed by this instance of InvalidJSONFileException.
     */
    private final String msg, itmsg;

    /**
     * The system's default locale.
     */
    private final Locale l;

    /**
     * This exception's constructor.
     * @param msg The English message. This value cannot be null or an empty string.
     * @param itmsg The Italian message. This value cannot be null or an empty string.
     * @throws InvalidArgumentException If any of the given arguments is null or an empty string
     */
    public InvalidJSONFileException(@NotNull String msg, @NotNull String itmsg) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(msg) || StringExt.checkNullOrEmpty(itmsg)) {
            throw new InvalidArgumentException("No arguments given to the InvalidJSONFileException's constructor can be " +
                    "null or an empty string.", "Nessuno degli argomenti forniti al costruttore di InvalidJDONFileException " +
                    "puo' essere null o una stringa vuota.");
        }

        this.msg = msg;
        this.itmsg = itmsg;
        l = Locale.getDefault();
    }

    /**
     * This method returns the message that was given to the constructor based on the system's default locale.
     * @return The message that was given to the constructor
     */
    @Override
    public String getMessage() {
        if(l == Locale.ITALIAN || l == Locale.ITALY) {
            return itmsg;
        }
        return msg;
    }

}
