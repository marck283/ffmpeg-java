package it.disi.unitn.exceptions;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * This class extends UnsupportedOperationException to minimise the lines of code to br written to throw it.
 */
public class UnsupportedOperationException extends java.lang.UnsupportedOperationException {

    private static final Locale l = Locale.getDefault();

    /**
     * This class's constructor.
     */
    public UnsupportedOperationException() {
        super();
    }

    /**
     * This class's constructor.
     * @param message The English message
     * @param itmsg The Italian message
     */
    public static void throwUnsupportedOperationException(@NotNull String message, @NotNull String itmsg) {
        if(l == Locale.ITALIAN || l == Locale.ITALY) {
            throw new java.lang.UnsupportedOperationException(itmsg);
        }
        throw new java.lang.UnsupportedOperationException(message);
    }
}
