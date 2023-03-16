package it.disi.unitn.exceptions;

import org.jetbrains.annotations.NotNull;

public class InvalidArgumentException extends Exception {
    private String message = "";

    public InvalidArgumentException(@NotNull String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
