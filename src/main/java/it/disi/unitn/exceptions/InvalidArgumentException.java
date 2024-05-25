package it.disi.unitn.exceptions;

import it.disi.unitn.StringExt;
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
     * The system's default locale.
     */
    private final Locale l;

    /**
     * Class constructor.
     * @param message The message given to the exception.
     * @param itmsg The message given to the exception translated in Italian
     */
    public InvalidArgumentException(@NotNull String message, @NotNull String itmsg) {
        l = Locale.getDefault();
        if(StringExt.checkNullOrEmpty(message) || StringExt.checkNullOrEmpty(itmsg)) {
            if(l == Locale.ITALY || l == Locale.ITALIAN) {
                System.err.println("Nessuno degli argomenti forniti al costruttore dell'eccezione InvalidArgumentException " +
                        "puo' essere null o una stringa vuota.");
            } else {
                System.err.println("No argument given to InvalidArgumentException's constructor can be null or an empty " +
                        "string.");
            }
            System.exit(2);
        }
        this.message = message;
        this.itmsg = itmsg;
    }

    /**
     * This method returns the message that was given to the constructor based on the system's default locale.
     * @return The message that was given to the constructor
     */
    public String getMessage() {
        if(l == Locale.ITALIAN || l == Locale.ITALY) {
            return itmsg;
        }
        return message;
    }
}
