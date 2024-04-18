package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.afreqshift;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;

/**
 * Apply frequency shift to input audio samples.
 */
public class AFreqShift extends AudioFilter {

    private double shift, level;

    private int order;

    /**
     * This class's constructor.
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public AFreqShift() throws InvalidArgumentException {
        super("afreqshift");
        shift = 0.0;
        level = 1.0;
        order = 8;
    }

    /**
     * Sets the "shift" value.
     * @param val The new "shift" value
     * @throws InvalidArgumentException If the new "shift" value is less than Integer.MIN_VALUE or greater than Integer.MAX_VALUE
     */
    public void setShift(double val) throws InvalidArgumentException {
        if(val < Integer.MIN_VALUE || val > Integer.MAX_VALUE) {
            throw new InvalidArgumentException("Shift value out of bounds.", "Il valore del parametro \"shift\" è fuori " +
                    "dall'intervallo dei valori ammessi.");
        }

        shift = val;
    }

    /**
     * Sets the output gain applied to the final output.
     * @param val The output gain value
     * @throws InvalidArgumentException If the output gain's value is less than 0.0 or greater than 1.0
     */
    public void setLevel(double val) throws InvalidArgumentException {
        if(val < 0.0 || val > 1.0) {
            throw new InvalidArgumentException("The given \"level\" value cannot be less than 0.0 or greater than 1.0.",
                    "Il valore del parametro \"level\" non puo' essere minore di 0.0 o maggiore di 1.0.");
        }

        level = val;
    }

    /**
     * Sets the "order" value.
     * @param val The new "order" value
     * @throws InvalidArgumentException If the new "order" value is less than 1 or greater than 16
     */
    public void setOrder(int val) throws InvalidArgumentException {
        if(val < 1 || val > 16) {
            throw new InvalidArgumentException("The \"order\" value cannot be less than 1 or greater than 16.", "Il valore " +
                    "del parametro \"value\" non puo' essere minore di 1 o maggiore di 16.");
        }

        order = val;
    }

    @Override
    public void updateMap() {
        options.put("shift", Double.toString(shift));
        options.put("level", Double.toString(level));
        options.put("order", Integer.toString(order));
    }
}
