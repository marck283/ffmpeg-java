package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.atempo;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;

/**
 * Implements the "atempo" audio filter.
 */
public class ATempo extends AudioFilter {

    private double val;

    /**
     * This class's constructor.
     *
     */
    public ATempo() {
        super("atempo");
        val = 1.0;
    }

    /**
     * Sets this filter's value.
     *
     * @param val This filter's value. Must be between 0.5 and 100.0
     * @throws InvalidArgumentException If the given value is less than 0.5 or greater than 100.0
     */
    public void setVal(double val) throws InvalidArgumentException {
        if (val < 0.5 || val > 100.0) {
            throw new InvalidArgumentException("The value given to the \"atempo\" filter cannot be less than 0.5 or " +
                    "greater than 100.0.", "Il valore fornito al filtro \"atempo\" non puo' essere minore di 0.5 o " +
                    "maggiore di 100.0.");
        }

        this.val = val;
    }

    @Override
    public void updateMap() {
        options.put("", String.valueOf(val));
    }
}
