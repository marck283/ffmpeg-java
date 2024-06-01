package it.disi.unitn.exceptions;

import it.disi.unitn.StringExt;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Wrapper class to implement exceptions in this library.
 */
public abstract class GeneralException extends Exception {

    /**
     * The message that will be printed by this instance of InvalidArgumentException.
     */
    protected final String message, itmsg;

    /**
     * The system's default locale.
     */
    protected final Locale l;

    /**
     * The class's constructor.
     * @param message The English message
     * @param itmsg The Italian message
     */
    protected GeneralException(@NotNull String message, @NotNull String itmsg) {
        l = Locale.getDefault();
        if(StringExt.checkNullOrEmpty(message) || StringExt.checkNullOrEmpty(itmsg)) {
            if(l == Locale.ITALY || l == Locale.ITALIAN) {
                System.err.println("Nessuno degli argomenti forniti al costruttore dell'eccezione GeneralException " +
                        "puo' essere null o una stringa vuota.");
            } else {
                System.err.println("No argument given to GeneralException's constructor can be null or an empty " +
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
    public abstract String getMessage();

}
