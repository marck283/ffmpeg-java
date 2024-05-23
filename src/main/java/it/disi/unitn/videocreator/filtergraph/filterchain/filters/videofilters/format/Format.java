package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.format;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the "format" video filter.
 */
public class Format extends VideoFilter {

    private final List<String> pix_fmts, col_spcs, col_rngs;

    /**
     * This class's constructor. Constructs a new "format" filter.
     * @throws InvalidArgumentException If the filter's name (given by this constructor to the superclass) is null or
     * an empty string
     */
    public Format() throws InvalidArgumentException {
        super("format");
        pix_fmts = new ArrayList<>();
        col_spcs = new ArrayList<>();
        col_rngs = new ArrayList<>();
    }

    /**
     * Adds the given pixel format to this filter.
     * @param pix_fmt The given pixel format. Must not be null or an empty string
     * @throws InvalidArgumentException If the given pixel format is null or an empty string
     */
    public void addPixelFormat(@NotNull String pix_fmt) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(pix_fmt)) {
            throw new InvalidArgumentException("The pixel format cannot be null or an empty string.", "Il formato dei " +
                    "pixel non puo' essere null o una stringa vuota.");
        }

        pix_fmts.add(pix_fmt);
    }

    /**
     * Removes the given pixel format (if found) from this filter.
     * @param pix_fmt The given pixel format. Must not be null or an empty string
     * @throws InvalidArgumentException If the given pixel format is null or an empty string
     */
    public void removePixelFormat(@NotNull String pix_fmt) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(pix_fmt)) {
            throw new InvalidArgumentException("The pixel format cannot be null or an empty string.", "Il formato dei " +
                    "pixel non puo' essere null o una stringa vuota.");
        }

        pix_fmts.remove(pix_fmt);
    }

    /**
     * Adds the given color space to this filter.
     * @param col_spc The given color space. Must not be null or an empty string
     * @throws InvalidArgumentException If the given color space is null or an empty string
     */
    public void addColorSpace(@NotNull String col_spc) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(col_spc)) {
            throw new InvalidArgumentException("The given color space cannot be null or an empty string.", "Il dato spazio " +
                    "dei colori non puo' essere null o una stringa vuota.");
        }

        col_spcs.add(col_spc);
    }

    /**
     * Removes the given color space from this filter.
     * @param col_spc The given color space. Must not be null or an empty string
     * @throws InvalidArgumentException If the given color space is null or an empty string
     */
    public void removeColorSpace(@NotNull String col_spc) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(col_spc)) {
            throw new InvalidArgumentException("The given color space cannot be null or an empty string.", "Il dato spazio " +
                    "dei colori non puo' essere null o una stringa vuota.");
        }

        col_spcs.remove(col_spc);
    }

    /**
     * Adds the given color range to this filter.
     * @param col_rng The given color range. Must not be null or an empty string
     * @throws InvalidArgumentException If the given color range is null or an empty string
     */
    public void addColorRange(@NotNull String col_rng) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(col_rng)) {
            throw new InvalidArgumentException("The given color range cannot be null or an empty string.", "Il dato " +
                    "intervallo dei colori non puo' essere null o una stringa vuota.");
        }

        col_rngs.add(col_rng);
    }

    /**
     * Removes the given color range from this filter.
     * @param col_rng The given color range. Must not be null or an empty string
     * @throws InvalidArgumentException If the given color range is null or an empty string
     */
    public void removeColorRange(@NotNull String col_rng) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(col_rng)) {
            throw new InvalidArgumentException("The given color range cannot be null or an empty string.", "Il dato " +
                    "intervallo dei colori non puo' essere null o una stringa vuota.");
        }

        col_rngs.remove(col_rng);
    }

    /**
     * Updates this filter's options.
     */
    public void updateMap() {
        options.put("pix_fmts", String.join("|", pix_fmts));
        options.put("color_spaces", String.join("|", col_spcs));
        options.put("color_ranges", String.join("|", col_rngs));
    }
}
