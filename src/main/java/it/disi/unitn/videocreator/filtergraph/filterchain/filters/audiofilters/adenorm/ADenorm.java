package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.adenorm;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements the FFmpeg "adenorm" filter.
 */
public class ADenorm extends AudioFilter {

    enum Type {
        DC, AC, SQUARE, PULSE;

        @Override
        public @NotNull String toString() {
            switch (this) {
                case AC -> {
                    return "ac";
                }
                case SQUARE -> {
                    return "square";
                }
                case PULSE -> {
                    return "pulse";
                }
                default -> {
                    return "dc";
                }
            }
        }
    }

    private int level;

    private Type type;

    /**
     * This class's constructor.
     *
     */
    public ADenorm() {
        super("adenorm");
        level = -351;
        type = Type.DC;
    }

    /**
     * Sets the level of added noise.
     *
     * @param val The new level of added noise
     * @throws InvalidArgumentException If the level of added noise is less than -351 or greater than -90
     */
    public void setLevel(int val) throws InvalidArgumentException {
        if (val < -351 || val > -90) {
            throw new InvalidArgumentException("The level of added noise cannot be less than -351 or greater than -90.",
                    "Il livello di rumore aggiunto non puo' essere inferiore a -351 o maggiore di -90.");
        }

        level = val;
    }

    /**
     * Sets the type of added noise.
     *
     * @param val The new type of added noise
     * @throws InvalidArgumentException If the given type of added noise is null, an empty string or a value not recognized
     *                                  by FFmpeg
     */
    public void setType(@NotNull String val) throws InvalidArgumentException {
        if (StringExt.checkNullOrEmpty(val)) {
            throw new InvalidArgumentException("The type of added noise cannot be null or an empty string.", "Il tipo di " +
                    "rumore aggiunto non puo' essere null o una stringa vuota.");
        }

        switch (val) {
            case "dc" -> type = Type.DC;
            case "ac" -> type = Type.AC;
            case "square" -> type = Type.SQUARE;
            case "pulse" -> type = Type.PULSE;
            default ->
                    throw new InvalidArgumentException("The type of added noise must be equal to \"dc\", \"ac\", \"square\" " +
                            "or \"pulse\".", "Il tipo di rumore aggiunto deve essere uguale a \"dc\", \"ac\", \"square\" o \"pulse\".");
        }
    }

    @Override
    public void updateMap() {
        options.put("level", String.valueOf(level));
        options.put("type", type.toString());
    }
}
