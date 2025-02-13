package it.disi.unitn.exceptions.transitions;

import it.disi.unitn.exceptions.GeneralException;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Exception specific for transitions.
 */
public class TranslationFailedException extends GeneralException {

    /**
     * The class's constructor.
     *
     * @param message The English message
     * @param itmsg   The Italian message
     */
    public TranslationFailedException(@NotNull String message, @NotNull String itmsg) {
        super(message, itmsg);
    }

    @Override
    public String getMessage() {
        return (l == Locale.ITALIAN || l == Locale.ITALY) ? itmsg : message;
    }
}
