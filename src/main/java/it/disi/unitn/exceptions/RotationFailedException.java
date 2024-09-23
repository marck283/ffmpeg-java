package it.disi.unitn.exceptions;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class RotationFailedException extends GeneralException{
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
        if(l == Locale.ITALIAN || l == Locale.ITALY) {
            return itmsg;
        }
        return message;
    }
}
