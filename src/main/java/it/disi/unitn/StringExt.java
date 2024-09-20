package it.disi.unitn;

import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

/**
 * This class provides some utility functions that can be used to extend the functionalities provided by the class String.
 */
public class StringExt {
    private String val;

    /**
     * The class's constructor.
     * @param str The String object with which to initialize this object
     * @throws InvalidArgumentException If the given argument is null or the empty string
     */
    public StringExt(@NotNull String str) throws InvalidArgumentException {
        if(checkNullOrEmpty(str)) {
            throw new InvalidArgumentException("The argument given to this constructor cannot be null nor can it be an " +
                    "empty string.", "L'argomento fornito a questo costruttore non puo' essere null o una stringa vuota.");
        }
        val = str;
    }

    /**
     * This method checks if the given string is null or empty.
     * @param str The given string.
     * @return True if the given string is null or empty
     */
    public static boolean checkNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * This method can be used to pad the given string with 0s at its beginning.
     * @throws InvalidArgumentException If the original string's length is not greater than 0 and less than or equal to 3.
     */
    public void padStart(int maxlen) throws InvalidArgumentException {
        if(val.isEmpty() || val.length() > maxlen) {
            throw new InvalidArgumentException("The original string's length is not greater than 0 and less than or " +
                    "equal to 3.", "La lunghezza della stringa originale non e' maggiore di zero e minore o uguale a 3.");
        }

        int missing = maxlen - val.length();
        if(missing > 0) {
            String v = "";
            for(int i = 0; i < missing; i++) {
                v = v.concat("0");
            }
            val = v.concat(val);
        }
    }

    /**
     * Getter method for the underlying string's value.
     * @return The underlying string's value
     */
    public String getVal() {
        return val;
    }
}