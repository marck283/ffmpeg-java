package it.disi.unitn.exceptions.transitions;

import it.disi.unitn.exceptions.GeneralException;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * This class implements an exception being thrown when the rotation fails for some reason.
 */
public class RotationFailedException extends GeneralException {

    /**
     * The class's constructor.
     *
     * @param message The English message
     * @param itmsg   The Italian message
     */
    public RotationFailedException(@NotNull String message, @NotNull String itmsg) {
        super(message, itmsg);
    }

    @Override
    public String getMessage() {
        return (l == Locale.ITALIAN || l == Locale.ITALY) ? itmsg : message;
    }
}
