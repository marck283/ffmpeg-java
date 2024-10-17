package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.adelay;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the "adelay" FFmpeg filter.
 */
public class ADelay extends AudioFilter {

    private final List<String> delList;

    private int all;

    /**
     * This class's constructor.
     *
     */
    public ADelay() {
        super("adelay");
        delList = new ArrayList<>();
        all = 0;
    }

    /**
     * Adds a new (channel, delay) pair.
     *
     * @param delay The new delay
     * @throws InvalidArgumentException If the new delay is null or an empty string
     */
    public void addDelay(@NotNull String delay) throws InvalidArgumentException {
        if (StringExt.checkNullOrEmpty(delay)) {
            throw new InvalidArgumentException("The given delay cannot be null or an empty string.", "Il ritardo fornito " +
                    "non puo' essere null o una stringa vuota.");
        }
        delList.add(delay);
    }

    /**
     * Adds a new (channel, delay) pair at a given position.
     *
     * @param i     The given position
     * @param delay The given delay
     * @throws InvalidArgumentException If the given position is negative or greater than or equal to the number of channels
     *                                  currently under delay, or if the given delay is null or an empty string
     */
    public void addDelay(int i, @NotNull String delay) throws InvalidArgumentException {
        if (i < 0 || i >= delList.size()) {
            throw new InvalidArgumentException("The given position cannot be negative or greater or equal to the list's " +
                    "size.", "La data posizione non puo' essere negativa o maggiore o uguale alla dimensione della lista.");
        }
        if (StringExt.checkNullOrEmpty(delay)) {
            throw new InvalidArgumentException("The given delay cannot be null or an empty string.", "Il ritardo fornito " +
                    "non puo' essere null o una stringa vuota.");
        }

        delList.add(i, delay);
    }

    /**
     * Sets the "all" parameter's value.
     *
     * @param val The new "all" parameter's value
     * @throws InvalidArgumentException If the given value is different from 0 or 1
     */
    public void setAll(int val) throws InvalidArgumentException {
        if (val != 0 && val != 1) {
            throw new InvalidArgumentException("The \"all\" parameter can only be equal to 0 or 1.", "Il valore del " +
                    "parametro \"all\" puo' essere uguale solo a 0 o 1.");
        }

        all = val;
    }

    @Override
    public void updateMap() {
        options.put("delays", String.join("|", delList));
        options.put("all", String.valueOf(all));
    }
}
