package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.acontrast;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;

/**
 * Simple audio dynamic range compression/expansion filter.
 */
public class AContrast extends Filter {

    private int contrast;

    /**
     * This class's constructor. Constructs a new filter (whether video or audio).
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public AContrast() throws InvalidArgumentException {
        super("acontrast");
        contrast = 33;
    }

    /**
     * Sets the contrast.
     * @param val The new contrast value
     * @throws InvalidArgumentException If the new contrast value is negative or greater than 100
     */
    public void setContrast(int val) throws InvalidArgumentException {
        if(val < 0 || val > 100) {
            throw new InvalidArgumentException("The contrast value cannot be negative or greater than 100.", "Il valore " +
                    "del contrasto non puo' essere negativo o maggiore di 100.");
        }

        contrast = val;
    }

    @Override
    public void updateMap() {
        options.put("contrast", String.valueOf(contrast));
    }
}
