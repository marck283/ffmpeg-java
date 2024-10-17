package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.buffer;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.UnsupportedOperationException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements FFmpeg's "buffer" filter.
 */
public class Buffer extends VideoFilter {

    private String video_size, pix_fmt, time_base, colorspace, range;

    private int width, height, frame_rate, sar;

    /**
     * The class's constructor
     */
    public Buffer() {
        super("buffer");
        video_size = "";
        width = 0;
        height = 0;
        pix_fmt = "";
        time_base = "";
        frame_rate = 0;
        colorspace = "";
        range = "";
        sar = 0;
    }

    /**
     * Sets the filter's video_size parameter. This cannot be done if the width and height parameters have already been
     * set.
     * @param width The video's width
     * @param height The video's height
     * @throws InvalidArgumentException If the video's width or height are less than or equal to zero
     * @throws UnsupportedOperationException If the video's width and height were already set by setVideoWidthAndHeight()
     */
    public void setVideoSize(int width, int height) throws InvalidArgumentException, UnsupportedOperationException {
        if(width <= 0 || height <= 0) {
            throw new InvalidArgumentException("The width and height cannot be less than or equal to zero.", "La larghezza " +
                    "e l'altezza non possono essere valori minori o uguali a zero.");
        }

        if(this.width > 0 && this.height > 0) {
            throw new UnsupportedOperationException();
        }

        video_size = width + "x" + height;
    }

    /**
     * Sets the input video's width and height. This cannot be done if the filter's video_size parameter was already set.
     * @param width The input video's width
     * @param height The input video's height
     * @throws InvalidArgumentException If the given width and height were less than or equal to zero
     * @throws UnsupportedOperationException If the filter's video_size parameter was already set
     */
    public void setVideoWidthAndHeight(int width, int height) throws InvalidArgumentException, UnsupportedOperationException {
        if(width <= 0 || height <= 0) {
            throw new InvalidArgumentException("The width and height cannot be less than or equal to zero.", "La larghezza " +
                    "e l'altezza non possono essere valori minori o uguali a zero.");
        }

        if(video_size != null && !video_size.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        this.width = width;
        this.height = height;
    }

    /**
     * Sets the filter's pix_fmt parameter.
     * @param pix_fmt The pixel's format
     * @throws InvalidArgumentException If the given pixel format is null or an empty string
     */
    public void setPixFmt(@NotNull String pix_fmt) throws InvalidArgumentException {
        if(pix_fmt == null || pix_fmt.isEmpty()) {
            throw new InvalidArgumentException("The pixel format cannot be null or an empty string.", "Il formato dei pixel " +
                    "non puo' essere null o una stringa vuota.");
        }

        this.pix_fmt = pix_fmt;
    }

    /**
     * Sets the filter's time_base parameter.
     * @param time_base The given time_base value
     * @throws InvalidArgumentException If the given time_base value is null or an empty string
     */
    public void setTimeBase(@NotNull String time_base) throws InvalidArgumentException {
        if(time_base == null || time_base.isEmpty()) {
            throw new InvalidArgumentException("The \"time_base\" parameter cannot be null or an empty string.", "Il valore " +
                    "del parametro \"time_base\" non puo' essere null o una stringa vuota.");
        }

        this.time_base = time_base;
    }

    /**
     * Sets the filter's frame rate value.
     * @param frame_rate The given frame rate
     * @throws InvalidArgumentException If the given frame rate is less than or equal to zero
     */
    public void setFrameRate(int frame_rate) throws InvalidArgumentException {
        if(frame_rate <= 0) {
            throw new InvalidArgumentException("The frame rate cannot be less than or equal to zero.", "Il frame rate non " +
                    "puo' essere minore o uguale a zero.");
        }

        this.frame_rate = frame_rate;
    }

    /**
     * Sets the filter's colorspace parameter.
     * @param colorspace The given colorspace value
     * @throws InvalidArgumentException If the given colorspace value is null or an empty string
     */
    public void setColorSpace(@NotNull String colorspace) throws InvalidArgumentException {
        if(colorspace == null || colorspace.isEmpty()) {
            throw new InvalidArgumentException("The color space parameter cannot be null or an empty string.", "Il parametro " +
                    "relativo allo spazio dei colori non puo' essere null o una stringa vuota.");
        }

        this.colorspace = colorspace;
    }

    /**
     * Sets the color's range.
     * @param range The given color range
     * @throws InvalidArgumentException If the given color range is null or an empty string
     */
    public void setColorRange(@NotNull String range) throws InvalidArgumentException {
        if(range == null || range.isEmpty()) {
            throw new InvalidArgumentException("The color's range cannot be null or an empty string.", "L'intervallo dei " +
                    "colori non puo' essere null o una stringa vuota.");
        }

        this.range = range;
    }

    /**
     * Sets the sample aspect ratio.
     * @param sar The given sample aspect ratio
     * @throws InvalidArgumentException If the given sample aspect ratio is less than or equal to zero
     */
    public void setSampleAspectRatio(int sar) throws InvalidArgumentException {
        if(sar <= 0) {
            throw new InvalidArgumentException("The sample aspect ratio cannot be less than or equal to zero.", "Il valore " +
                    "del parametro \"Sample Aspect Ratio\" non puo' essere minore o uguale a zero.");
        }

        this.sar = sar;
    }

    @Override
    public void updateMap() {
        if(video_size != null && !video_size.isEmpty()) {
            options.put("video_size", video_size);
        } else {
            options.put("width", String.valueOf(width));
            options.put("height", String.valueOf(height));
        }
        if(pix_fmt != null && !pix_fmt.isEmpty()) {
            options.put("pix_fmt", pix_fmt);
        }
        if(time_base != null && !time_base.isEmpty()) {
            options.put("time_base", time_base);
        }
        if(frame_rate > 0) {
            options.put("frame_rate", String.valueOf(frame_rate));
        }
        if(colorspace != null && !colorspace.isEmpty()) {
            options.put("colorspace", colorspace);
        }
        if(range != null && !range.isEmpty()) {
            options.put("range", range);
        }
        if(sar > 0) {
            options.put("sar", String.valueOf(sar));
        }
    }
}
