package it.disi.unitn.videocreator.filtergraph.filterchain.filters.multimedia.rotate;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.multimedia.rotate.color.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class implements FFmpeg's "rotate" filter.
 */
public class Rotate extends Filter {

    private @NotNull String angle, ow, oh;

    private int bilinear;

    private final @NotNull Color color;

    /**
     * This class's constructor. Constructs a new "rotate" filter.
     */
    public Rotate() {
        super("rotate");
        this.angle = "0";
        this.ow = "iw";
        this.oh = "ih";
        bilinear = 1;
        color = new Color();
    }

    /**
     * This method sets the rotation angle.
     * @param angle The given angle. This value cannot be null or an empty string.
     * @throws InvalidArgumentException If the given angle is null or an empty string
     */
    public void setAngle(@NotNull String angle) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(angle)) {
            throw new InvalidArgumentException("The given angle cannot be null or an mpty string.", "L'angolo fornito " +
                    "non puo' essere null o una stringa vuota.");
        }

        this.angle = angle;
    }

    /**
     * This method sets the output frame's size. Both arguments can be null or empty strings, in which case this filter's
     * "ow" and "oh" parameters will default to "iw" and "ih" respectively.
     * @param ow The output frame's width.
     * @param oh The output frame's height.
     */
    public void setOutputSize(@Nullable String ow, @Nullable String oh) {
        if(StringExt.checkNullOrEmpty(ow)) {
            this.ow = "iw";
        } else {
            this.ow = ow;
        }

        if(StringExt.checkNullOrEmpty(oh)) {
            this.oh = "ih";
        } else {
            this.oh = oh;
        }
    }

    /**
     * This method sets the filter's "bilinear" parameter's value.
     * @param bilinear The given bilinear value. This value can be either 0 (disabled bilinear interpolation) or 1
     *                 (enabled bilinear interpolation).
     * @throws InvalidArgumentException If the given value is different from 0 or 1
     */
    public void setBilinear(int bilinear) throws InvalidArgumentException {
        if(bilinear != 0 && bilinear != 1) {
            throw new InvalidArgumentException("Invalid bilinear value.", "Valore non valido per il parametro \"bilinear\".");
        }

        this.bilinear = bilinear;
    }

    /**
     * Ths method sets this filter's color.
     * @param color The given color. This value cannot be null oran empty string, and it must conform to FFmpeg's
     *              requirements. See <a href="https://ffmpeg.org/ffmpeg-utils.html#color-syntax">here</a> for the
     *              accepted color names. The special value "none" tells the program not to print any background.
     * @throws InvalidArgumentException If the given argument is null, an empty string, or it does not conform to the
     * list of accepted names (including the special value "none")
     */
    public void setColor(@NotNull String color) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(color)) {
            throw new InvalidArgumentException("The given color cannot be null or an empty string.", "Il colore richiesto " +
                    "non puo' essere null o una stringa vuota.");
        }

        this.color.setColor(color);
    }

    @Override
    public void updateMap() {
        options.put("angle", angle);
        options.put("ow", ow);
        options.put("oh", oh);
        options.put("bilinear", String.valueOf(bilinear));
        options.put("color", color.toString());
    }
}
