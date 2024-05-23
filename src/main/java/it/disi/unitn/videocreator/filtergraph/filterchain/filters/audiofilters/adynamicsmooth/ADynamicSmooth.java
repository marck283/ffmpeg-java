package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.adynamicsmooth;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;

/**
 * Apply dynamic smoothing to input audio stream.
 */
public class ADynamicSmooth extends AudioFilter {

    private long sensitivity, basefreq;

    /**
     * This class's constructor.
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public ADynamicSmooth() throws InvalidArgumentException {
        super("adynamicsmooth");
        sensitivity = 2L;
        basefreq = 22050L;
    }

    /**
     * Sets the value of the sensitivity to frequency fluctuations.
     * @param val The new sensitivity value
     * @throws InvalidArgumentException If the given sensitivity value is negative or greater than 1000000
     */
    public void setSensitivity(long val) throws InvalidArgumentException {
        if(val < 0L || val > 1000000L) {
            throw new InvalidArgumentException("The value of the sensitivity to frequency fluctuations cannot be negative " +
                    "or greater than 1000000.", "Il valore della sensibilita' alle fluttuazioni di frequenza non puo' " +
                    "essere negativo o maggiore di 1000000.");
        }

        sensitivity = val;
    }

    /**
     * Sets the value of the base frequency for smoothing.
     * @param val The new base frequency value
     * @throws InvalidArgumentException If the given base frequency value is less than 2 or greater than 1000000
     */
    public void setBasefreq(long val) throws InvalidArgumentException {
        if(val < 2L || val > 1000000L) {
            throw new InvalidArgumentException("The value of the base frequency for smoothing cannot be less than 2 or " +
                    "greater than 1000000.", "Il valore della frequenza di base per l'appiattimento non puo' essere " +
                    "minore di 2 o maggiore di 1000000.");
        }

        basefreq = val;
    }

    @Override
    public void updateMap() {
        options.put("sensitivity", String.valueOf(sensitivity));
        options.put("basefreq", String.valueOf(basefreq));
    }
}
