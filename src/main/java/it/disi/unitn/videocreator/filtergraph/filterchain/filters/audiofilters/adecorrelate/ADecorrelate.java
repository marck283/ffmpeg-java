package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.adecorrelate;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;

/**
 * Apply decorrelation to input audio stream.
 */
public class ADecorrelate extends Filter {

    private int stages = 6;

    private long seed = -1;

    /**
     * This class's constructor. Constructs a new filter (whether video or audio).
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    protected ADecorrelate() throws InvalidArgumentException {
        super("adecorrelate");
    }

    /**
     * Sets the number of stages.
     * @param val The new number of stages
     * @throws InvalidArgumentException If the new number of stages is less than 1 or greater than 16
     */
    public void setStages(int val) throws InvalidArgumentException {
        if(val < 1 || val > 16) {
            throw new InvalidArgumentException("The number of stages cannot be less than 1 or greater than 16.", "Il numero " +
                    "di passaggi non puo' essere minore di 1 o maggiore di 16.");
        }

        stages = val;
    }

    /**
     * Sets the seed.
     * @param val The new seed
     * @throws InvalidArgumentException If the new seed is less than -1 or greater than 4294967295
     */
    public void setSeed(long val) throws InvalidArgumentException {
        if(val < -1 || val > 4294967295L) {
            throw new InvalidArgumentException("The seed cannot be less than -1 or greater than 4294967295.", "Il seed " +
                    "non puo' essere minore di -1 o maggiore di 4294967295.");
        }

        seed = val;
    }

    @Override
    protected void updateMap() {
        options.put("stages", String.valueOf(stages));
        options.put("seed", String.valueOf(seed));
    }
}
