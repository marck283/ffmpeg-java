package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.acrossfade;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements the "acrossfade" audio filter.
 */
public class ACrossFade extends AudioFilter {

    private long nb_samples;

    private final Duration duration;

    private int overlap;

    private Curve curve1, curve2;

    /**
     * This class's constructor.
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public ACrossFade() throws InvalidArgumentException {
        super("acrossfade");
        nb_samples = 44100;
        duration = new Duration();
        overlap = 0b1;
        curve1 = new Curve();
        curve2 = new Curve();
    }

    /**
     * This method set the nb_samples value.
     * @param nb_samples The given nb_samples value. This value must not be negative.
     * @throws InvalidArgumentException If the given value is negative
     */
    public void setNbSamples(long nb_samples) throws InvalidArgumentException {
        if(nb_samples < 0L) {
            throw new InvalidArgumentException("The \"nb_samples\" value must not be negative.", "Il valore \"nb_samples\" " +
                    "non puo' essere negativo.");
        }

        this.nb_samples = nb_samples;
    }

    /**
     * This method sets the duration's number of hours.
     * @param hours The given number of hours. This value cannot be negative or greater than 23.
     * @throws InvalidArgumentException If the given value is negative or greater than 23
     */
    public void setHours(int hours) throws InvalidArgumentException {
        duration.setHours(hours);
    }

    /**
     * This method sets the duration's number of minutes.
     * @param minutes The given number of minutes. This value cannot be negative or greater than 59.
     * @throws InvalidArgumentException If the given value is negative or greater than 59
     */
    public void setMinutes(int minutes) throws InvalidArgumentException {
        duration.setMinutes(minutes);
    }

    /**
     * This method sets the duration's number of seconds.
     * @param seconds The given number of seconds. This value cannot be negative or greater than 59.
     * @throws InvalidArgumentException If the given value is negative or greater than 59
     */
    public void setSeconds(int seconds) throws InvalidArgumentException {
        duration.setSeconds(seconds);
    }

    /**
     * This method sets the decimal part of the duration.
     * @param decimal The given decimal value as an integer. This value must not be negative.
     * @throws InvalidArgumentException If the given value is negative
     */
    public void setDecimal(long decimal) throws InvalidArgumentException {
        duration.setDecimal(decimal);
    }

    /**
     * This method sets the overlap parameter.
     * @param overlap The given overlap boolean value.
     */
    public void setOverlap(boolean overlap) {
        this.overlap = (overlap ? 0b1 : 0);
    }

    private void checkCurve(@NotNull Curve curve) throws InvalidArgumentException {
        if(curve == null) {
            throw new InvalidArgumentException("The given Curve instance cannot be null.", "L'istanza di Curve fornita " +
                    "non puo' essere null.");
        }
    }

    /**
     * This method sets the "curve1" parameter's value.
     * @param curve1 The given {@link Curve} instance. This value cannot be null.
     * @throws InvalidArgumentException If the given {@link Curve} instance is null
     */
    public void setCurve1(@NotNull Curve curve1) throws InvalidArgumentException {
        checkCurve(curve1);
        this.curve1 = curve1;
    }

    /**
     * This method sets the "curve2" parameter's value.
     * @param curve2 The given {@link Curve} instance. This value cannot be null.
     * @throws InvalidArgumentException If the given {@link Curve} instance is null
     */
    public void setCurve2(@NotNull Curve curve2) throws InvalidArgumentException {
        checkCurve(curve2);
        this.curve2 = curve2;
    }

    @Override
    public void updateMap() {
        options.put("nb_samples", String.valueOf(nb_samples));
        options.put("duration", duration.toString());
        options.put("overlap", String.valueOf(overlap));

        String c1 = curve1.toString(), c2 = curve2.toString();
        if(c1 != null) {
            options.put("curve1", c1);
        }
        if(c2 != null) {
            options.put("curve2", c2);
        }
    }
}
