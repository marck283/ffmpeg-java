package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.colorcorrect;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements the "colorcorrect" video filter.
 */
public class ColorCorrect extends Filter {

    private double rl = 0, bl = 0, rh = 0, bh = 0, saturation = 1.0;

    private String analyze;

    /**
     * This class's constructor. Constructs a new filter (whether video or audio).
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    protected ColorCorrect() throws InvalidArgumentException {
        super("colorcorrect");
    }

    /**
     * Sets the red shadow spot.
     * @param rl The new red shadow spot
     * @throws InvalidArgumentException If the given red shadow spot is less than -1.0 or greater than 1.0
     */
    public void serRL(double rl) throws InvalidArgumentException {
        if(rl < -1.0 || rl > 1.0) {
            throw new InvalidArgumentException("The red shadow spot cannot be less than -1.0 or greater than 1.0.", "Il " +
                    "punto d'ombra del rosso non puo' essere inferiore a -1.0 o maggiore a 1.0.");
        }
        this.rl = rl;
    }

    /**
     * Sets the blue shadow spot.
     * @param bl The new blue shadow spot
     * @throws InvalidArgumentException If the blue shadow spot is less than -1.0 or greater than 1.0
     */
    public void setBL(double bl) throws InvalidArgumentException {
        if(bl < -1.0 || bl > 1.0) {
            throw new InvalidArgumentException("The blue shadow spot cannot be less than -1.0 or greater than 1.0.", "Il " +
                    "punto d'ombra del blu non puo' essere minore di -1.0 o maggiore di 1.0.");
        }
        this.bl = bl;
    }

    /**
     * Sets the red highlight spot.
     * @param rh The new red highlight spot
     * @throws InvalidArgumentException If the given red highlight spot is less than -1.0 or greater than 1.0
     */
    public void setRH(double rh) throws InvalidArgumentException {
        if(rh < -1.0 || rh > 1.0) {
            throw new InvalidArgumentException("The red highlight spot cannot be less than -1,0 or greater than 1.0.",
                    "Il punto d'evidenza del rosso non puo' essere minore di -1.0 o maggiore di 1.0.");
        }
        this.rh = rh;
    }

    /**
     * Sets the blue highlight spot.
     * @param bh The new blue highlight spot
     * @throws InvalidArgumentException If the given blue highlight spot is less than -1.0 or greater than 1.0
     */
    public void setBH(double bh) throws InvalidArgumentException {
        if(bh < -1.0 || bh > 1.0) {
            throw new InvalidArgumentException("The blue highlight spot cannot be less than -1.0 or greater than 1.0.",
                    "Il punto d'evidenza del blu non puo' essere minore di -1.0 o maggiore di 1.0.");
        }
        this.bh = bh;
    }

    /**
     * Sets the saturation value.
     * @param saturation The new saturation value
     * @throws InvalidArgumentException If the saturation value is less than -1.0 or greater than 1.0
     */
    public void setSaturation(double saturation) throws InvalidArgumentException {
        if(saturation < -1.0 || saturation > 1.0) {
            throw new InvalidArgumentException("The saturation value cannot be less than -1.0 or greater than 1.0.", "Il " +
                    "valore della saturazione non puo' essere minore di -1.0 o maggiore di 1.0.");
        }
        this.saturation = saturation;
    }

    /**
     * Sets the "analyze" value.
     * @param analyze The new "analyze" value
     * @throws InvalidArgumentException If the new "analyze" value is different from "manual", "average", "minmax" or
     * "median"
     */
    public void setAnalyze(@NotNull String analyze) throws InvalidArgumentException {
        if(!(analyze.equals("manual") || analyze.equals("average") || analyze.equals("minmax") || analyze.equals("median"))) {
            throw new InvalidArgumentException("The \"analyze\" value must be equal to \"manual\", \"average\", \"minmax\" " +
                    "or \"median\".", "Il valore del parametro \"analyze\" deve essere uguale a \"manual\", \"average\", " +
                    "\"minmax\" o \"median\".");
        }
        this.analyze = analyze;
    }

    /**
     * Updates the options using the newly set values.
     */
    public void updateMap() {
        options.put("rl", String.valueOf(rl));
        options.put("bl", String.valueOf(bl));
        options.put("rh", String.valueOf(rh));
        options.put("bh", String.valueOf(bh));
        options.put("saturation", String.valueOf(saturation));
        options.put("analyze", analyze);
    }
}
