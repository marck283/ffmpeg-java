package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.acrossfade;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements the "duration" field of the "acrossfade" audio filter.
 */
class Duration {

    private int hours, minutes, seconds;

    private long decimal; //The decimal part

    /**
     * The class's constructor.
     */
    Duration() {
        hours = 0;
        minutes = 0;
        seconds = 0;
        decimal = 0L;
    }

    /**
     * This method sets the number of hours.
     *
     * @param hours The given number of hours. This value must not be negative or greater than 23.
     * @throws InvalidArgumentException If the given number of hours is negative or greater than 23
     */
    public void setHours(int hours) throws InvalidArgumentException {
        if (hours < 0 || hours > 23) {
            throw new InvalidArgumentException("The given number of hours must not be negative or greater than 23.",
                    "Il numero di ore indicato non puo' essere negativo o maggiore di 23.");
        }

        this.hours = hours;
    }

    /**
     * This method sets the number of minutes.
     *
     * @param minutes The given number of minutes. This value must not be negative or greater than 59.
     * @throws InvalidArgumentException If the given value is negative or greater than 59
     */
    public void setMinutes(int minutes) throws InvalidArgumentException {
        if (minutes < 0 || minutes > 59) {
            throw new InvalidArgumentException("The given number of minutes must not be negative or greater than 59.",
                    "Il numero di minuti indicato non puo' essere negativo o maggiore di 59.");
        }

        this.minutes = minutes;
    }

    /**
     * This method sets the number of seconds.
     *
     * @param seconds The given number of seconds. This value must not be negative or greater than 59.
     * @throws InvalidArgumentException If the given value is negative or greater than 59
     */
    public void setSeconds(int seconds) throws InvalidArgumentException {
        if (seconds < 0 || seconds > 59) {
            throw new InvalidArgumentException("The given number of seconds cannot be negative or greater than 59.",
                    "Il numero di secondi indicato non puo' essere negativo o maggiore di 59.");
        }

        this.seconds = seconds;
    }

    /**
     * This method sets the decimal part of the duration.
     *
     * @param decimal The given decimal value. This value must not be negative.
     * @throws InvalidArgumentException If the given value is negative
     */
    public void setDecimal(long decimal) throws InvalidArgumentException {
        if (decimal < 0) {
            throw new InvalidArgumentException("The decimal part cannot be negative.", "La parte decimale non puo' " +
                    "essere negativa.");
        }

        this.decimal = decimal;
    }

    private void padString(@NotNull StringExt str) {
        if (str.getVal().length() < 2) {
            str.padStart(2);
        }
    }

    public String toString() {
        StringExt hrs = new StringExt(String.valueOf(hours)), mins = new StringExt(String.valueOf(minutes)),
                secs = new StringExt(String.valueOf(seconds));

        padString(hrs);
        padString(mins);
        padString(secs);

        return hrs.getVal() + ":" + mins.getVal() + ":" + secs.getVal() + "." + decimal;
    }

}
