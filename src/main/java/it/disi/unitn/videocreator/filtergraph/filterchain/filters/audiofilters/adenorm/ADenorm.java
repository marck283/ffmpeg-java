package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.adenorm;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;
import org.jetbrains.annotations.NotNull;

public class ADenorm extends AudioFilter {

    private int level = -351;

    private String type = "dc";

    /**
     * This class's constructor.
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public ADenorm() throws InvalidArgumentException {
        super("adenorm");
    }

    /**
     * Sets the level of added noise.
     * @param val The new level of added noise
     * @throws InvalidArgumentException If the level of added noise is less than -351 or greater than -90
     */
    public void setLevel(int val) throws InvalidArgumentException {
        if(val < -351 || val > -90) {
            throw new InvalidArgumentException("The level of added noise cannot be less than -351 or greater than -90.",
                    "Il livello di rumore aggiunto non puo' essere inferiore a -351 o maggiore di -90.");
        }

        level = val;
    }

    /**
     * Sets the type of added noise.
     * @param val The new type of added noise
     * @throws InvalidArgumentException If the given type of added noise is null, an empty string or a value not recognized
     * by FFmpeg
     */
    public void setType(@NotNull String val) throws InvalidArgumentException {
        if(val == null || val.isEmpty()) {
            throw new InvalidArgumentException("The type of added noise cannot be null or an empty string.", "Il tipo di " +
                    "rumore aggiunto non puo' essere null o una stringa vuota.");
        }

        switch(val) {
            case "dc", "ac", "square", "pulse" -> type = val;
            default -> throw new InvalidArgumentException("The type of added noise must be equal to \"dc\", \"ac\", \"square\" " +
                    "or \"pulse\".", "Il tipo di rumore aggiunto deve essere uguale a \"dc\", \"ac\", \"square\" o \"pulse\".");
        }
    }

    @Override
    protected void updateMap() {
        options.put("level", String.valueOf(level));
        options.put("type", type);
    }
}
