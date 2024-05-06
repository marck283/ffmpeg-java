package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.drawtext.timecode;

import it.disi.unitn.exceptions.InvalidArgumentException;

public class TimeCode {
    private int hours, minutes, seconds;

    private double rate;

    public TimeCode() {
        hours = 0;
        minutes = 0;
        seconds = 0;
        rate = 1.0;
    }

    /**
     * Sets the number of hours to be used in the "timecode" option of the "drawtext" filter.
     * @param hours The given number of hours. This value must be between 0 and 23.
     * @throws InvalidArgumentException If the given value is less than 0 or greater than 23
     */
    public void setHours(int hours) throws InvalidArgumentException {
        if(hours < 0 || hours > 23) {
            throw new InvalidArgumentException("The number of hours must be between 0 and 23.", "Il numero di ore deve " +
                    "essere compreso tra 0 e 23.");
        }

        this.hours = hours;
    }

    /**
     * Sets the number of minutes to be used in the "timecode" option of the "drawtext" filter.
     * @param minutes The given number of minutes. This value must be between 0 and 59.
     * @throws InvalidArgumentException If the given value is less than 0 or greater than 59
     */
    public void setMinutes(int minutes) throws InvalidArgumentException {
        if(minutes < 0 || minutes > 59) {
            throw new InvalidArgumentException("The number of minutes must be between 0 and 59.", "Il numero di " +
                    "minuti deve essere compreso tra 0 e 59.");
        }

        this.minutes = minutes;
    }

    /**
     * Sets the number of seconds to be used in the "timecode" option of the "drawtext" filter.
     * @param seconds The given number of seconds. This value must be between 0 and 59.
     * @throws InvalidArgumentException If the given value is less than 0 or greater than 59
     */
    public void setSeconds(int seconds) throws InvalidArgumentException {
        if(seconds < 0 || seconds > 59) {
            throw new InvalidArgumentException("The number of seconds must be between 0 and 59.", "Il numero " +
                    "di secondi deve essere compreso tra 0 e 59.");
        }

        this.seconds = seconds;
    }

    /**
     * Sets the "timecode_rate" option's value to be used in the "drawtext" filter.
     * @param rate The given "timecode_rate" value. This value cannot be less than 1.0.
     * @throws InvalidArgumentException If the given value is less than 1.0
     */
    public void setRate(double rate) throws InvalidArgumentException {
        if(rate < 1.0) {
            throw new InvalidArgumentException("The given \"timecode_rate\" value cannot be less than 1.0.", "Il valore " +
                    "dell'opzione \"timecode_rate\" non puo' essere minore di 1.0.");
        }

        this.rate = rate;
    }

    /**
     * Returns the "timecode_rate" value set with setRate().
     * @return The "timecode_rate" value set with setRate().
     */
    public double getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return hours + ":" + minutes + ":" + seconds + ":ff";
    }
}
