package it.disi.unitn;

import com.google.common.base.Strings;
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
     */
    public StringExt(@NotNull String str) {
        if(checkNullOrEmpty(str)) {
            System.err.println((new InvalidArgumentException("The argument given to this constructor cannot be null nor " +
                    "can it be an empty string.", "L'argomento fornito a questo costruttore non puo' essere null o una " +
                    "stringa vuota.")).getMessage());
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
     * @param maxlen The given string's maximum length.
     */
    public void padStart(int maxlen) {
        if(val.isEmpty() || val.length() > maxlen) {
            System.err.println((new InvalidArgumentException("The original string's length is not greater than 0 and less " +
                    "than or equal to 3.", "La lunghezza della stringa originale non e' maggiore di zero e minore o " +
                    "uguale a 3.")).getMessage());
        } else {
            val = Strings.padStart(val, 3, '0');
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