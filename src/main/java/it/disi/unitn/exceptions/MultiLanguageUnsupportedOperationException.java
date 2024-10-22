package it.disi.unitn.exceptions;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * This class extends MultiLanguageUnsupportedOperationException to minimise the lines of code to br written to throw it.
 */
public class MultiLanguageUnsupportedOperationException extends GeneralException {

    private static final Locale l = Locale.getDefault();

    /**
     * This class's constructor.
     */
    public MultiLanguageUnsupportedOperationException(@NotNull String msg, @NotNull String itmsg) {
        super(msg, itmsg);
    }

    @Override
    public String getMessage() {
        return (l == Locale.ITALY || l == Locale.ITALIAN) ? itmsg : message;
    }

    /**
     * This class's constructor.
     * @param message The English message
     * @param itmsg The Italian message
     */
    public static void throwUnsupportedOperationException(@NotNull String message, @NotNull String itmsg) throws MultiLanguageUnsupportedOperationException {
        throw new MultiLanguageUnsupportedOperationException(message, itmsg);
    }
}
