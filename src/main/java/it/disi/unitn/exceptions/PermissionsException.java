package it.disi.unitn.exceptions;

import it.disi.unitn.StringExt;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Locale;

/**
 * This class provides an exception to tell the user that he or she does not have enough permissions to act on a specific
 * location inside the file system.
 */
public class PermissionsException extends IOException {

    /**
     * The message that will be printed by this instance of PermissionsException.
     */
    private final String msg, itmsg;

    /**
     * The system's default locale.
     */
    private final Locale l;

    /**
     * This class's constructor.
     * @param msg The English message to be printed. This value cannot be null or an empty string.
     * @param itmsg The Italian message to be printed. This value cannot be null or an empty string.
     */
    public PermissionsException(@NotNull String msg, @NotNull String itmsg) {
        if(StringExt.checkNullOrEmpty(msg) || StringExt.checkNullOrEmpty(itmsg)) {
            System.err.println((new InvalidArgumentException("No argument given to this exception's constructor can be " +
                    "null or an empty string.", "Nessun argomento fornito al costruttore di questa eccezione puo' essere " +
                    "null o una stringa vuota.")).getMessage());
        }

        this.msg = msg;
        this.itmsg = itmsg;
        l = Locale.getDefault();
    }

    /**
     * This method returns the exception's message based on the default system language.
     * @return The exception's message based on the default system language
     */
    @Override
    public String getMessage() {
        if(l == Locale.ITALIAN || l == Locale.ITALY) {
            return itmsg;
        }
        return msg;
    }

}
