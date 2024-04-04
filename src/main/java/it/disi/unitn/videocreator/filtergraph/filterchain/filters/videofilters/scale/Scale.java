package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.scalingalgs.ScalingAlgorithm;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements the "scale" video filter.
 */
public class Scale extends VideoFilter {

    private ScalingAlgorithm sws_flags; //These flags will then be joined by using String.join("+", sws_flags)

    private final boolean src_range, dst_range; //false = 0

    private final String width, height;

    private String sws_dither = "auto", alphablend = "none";

    /**
     * This class's constructor. Constructs a new "scale" filter.
     * @param width The given width
     * @param height The given height
     * @param src_range The given input range
     * @param dst_range The given output range
     * @throws InvalidArgumentException If the filter's name (given by this constructor to the superclass) is null or
     * an empty string
     */
    public Scale(@NotNull String width, @NotNull String height, boolean src_range, boolean dst_range) throws InvalidArgumentException {
        super("scale");
        this.width = width;
        this.height = height;
        this.src_range = src_range;
        this.dst_range = dst_range;
    }

    /**
     * Sets the sws_flags parameter.
     * @param val The ScalingAlgorithm instance to be used
     * @throws InvalidArgumentException If the given ScalingAlgorithm instance is null
     */
    public void setSwsFlags(@NotNull ScalingAlgorithm val) throws InvalidArgumentException {
        String algName = val.getName();
        if(algName.isEmpty()) {
            throw new InvalidArgumentException("The given \"sws flags\" value cannot be an empty string.", "Il " +
                    "valore \"sws flags\" fornito non puo' essere null o una stringa vuota.");
        }

        switch(algName) {
            case "fast_bilinear", "bilinear", "bicubic", "experimental", "neighbor", "area", "bicublin", "gauss", "sinc",
                    "lanczos", "spline", "print_info", "accurate_rnd", "full_chroma_int", "full_chroma_inp", "bitexact" -> sws_flags = val;
            default -> throw new InvalidArgumentException("Illegal sws_flags value.", "Valore sws_flags non permesso.");
        }
    }

    /**
     * Sets the sws_dither parameter.
     * @param dither The sws_dither value to be used
     * @throws InvalidArgumentException If the given value is null or an empty string
     */
    public void setSwsDither(@NotNull String dither) throws InvalidArgumentException {
        if(dither == null || dither.isEmpty()) {
            throw new InvalidArgumentException("The given \"sws dither\" value cannot be null or an empty string.", "Il " +
                    "valore \"sws dither\" fornito non puo' essere null o una stringa vuota.");
        }

        switch(dither) {
            case "auto", "none", "bayer", "ed", "a_dither", "x_dither" -> sws_dither = dither;
            default -> throw new InvalidArgumentException("Illegal sws_dither value.", "Valore sws_dither non permesso.");
        }
    }

    /**
     * Sets the alphablend value.
     * @param blend The alphablend value to be used
     * @throws InvalidArgumentException If the given value is null or an empty string
     */
    public void setAlphablend(@NotNull String blend) throws InvalidArgumentException {
        if(blend == null || blend.isEmpty()) {
            throw new InvalidArgumentException("The given \"alphablend\" value cannot be null or an empty string.", "Il " +
                    "valore \"alphablend\" fornito non puo' essere null o una stringa vuota.");
        }

        switch(blend) {
            case "uniform_color", "checkerboard", "none" -> alphablend = blend;
            default -> throw new InvalidArgumentException("Illegal alphablend value.", "Valore alphablend non permesso.");
        }
    }

    /**
     * Fills the Map declared in Filter with the correct values.
     */
    public void updateMap() {
        options.put("width", width);
        options.put("height", height);
        if(sws_flags != null) {
            options.put("flags", sws_flags.toString());
        }
        if(src_range) {
            options.put("src_range", "1");
        } else {
            options.put("src_range", "0");
        }
        if(dst_range) {
            options.put("dst_range", "1");
        } else {
            options.put("dst_range", "0");
        }
        options.put("sws_dither", sws_dither);
        options.put("alphablend", alphablend);
    }
}
