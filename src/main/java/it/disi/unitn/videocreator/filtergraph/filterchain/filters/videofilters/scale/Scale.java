package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale;

import it.disi.unitn.ProcessController;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import it.disi.unitn.exceptions.UnsupportedOperationException;
import it.disi.unitn.videocreator.ExecutorResHandler;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.scalingalgs.ScalingAlgorithm;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Locale;

/**
 * This class implements the "scale" video filter.
 */
public class Scale extends VideoFilter {

    private final ScalingParams scalingParams;

    private final Locale l;

    /**
     * This class's constructor. Constructs a new "scale" filter.
     * @throws InvalidArgumentException If the filter's name (given by this constructor to the superclass) is null or
     * an empty string
     */
    public Scale() throws InvalidArgumentException {
        super("scale");
        scalingParams = new ScalingParams();
        l = Locale.getDefault();
    }

    /**
     * The class's constructor given pre-established scaling parameters.
     * @param scalingParams The given ScalingParams instance. This value cannot be null.
     * @throws InvalidArgumentException If the given value is null
     */
    public Scale(@NotNull ScalingParams scalingParams) throws InvalidArgumentException {
        super("scale");

        if(scalingParams == null) {
            throw new InvalidArgumentException("The scaling parameters argument given to this constructor cannot be null.",
                    "L'istanza dei parametri di scaling fornita a questo costruttore non puo' essere null.");
        }

        this.scalingParams = scalingParams;
        l = Locale.getDefault();
    }

    /**
     * Sets the sws_flags parameter.
     * @param val The ScalingAlgorithm instance to be used
     * @throws InvalidArgumentException If the given ScalingAlgorithm instance is null
     */
    public void setSwsFlags(@NotNull ScalingAlgorithm val) throws InvalidArgumentException {
        scalingParams.setSwsFlags(val);
    }

    /**
     * This method executes the given Command Line command.
     *
     * @param executor       A DefaultExecutor instance
     * @param execResHandler An ExecutorResHandler instance
     * @param cmdline        A CommandLine instance
     * @return True if the CommandLine instance has a field "value" whose value is equal to zero, otherwise false
     */
    private boolean executeCommand(@NotNull DefaultExecutor executor, @NotNull ExecutorResHandler execResHandler,
                               @NotNull CommandLine cmdline) {
        try {
            ProcessController controller = new ProcessController(executor, execResHandler);
            int val = controller.execute(cmdline);
            if (val == 0) {
                if (l == Locale.ITALY || l == Locale.ITALIAN) {
                    System.err.println("Almeno uno dei valori forniti non e' stato riconosciuto da FFmpeg.");
                } else {
                    System.err.println("At least one of the given values was not recognised by FFmpeg.");
                }
                return false;
            }
            return true;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sets the width and height of the video.
     * @param development A boolean parameter to tell the program if the user is running a custom version of FFmpeg
     * @param width The video's width
     * @param height The video's height
     * @param pix_fmt The video's pixel format
     * @throws InvalidArgumentException If the video's with or height is null or an empty string, or the given pixel
     * format is null or an empty string
     * @throws UnsupportedOperationException If the video's size id is already set
     * @throws UnsupportedOperatingSystemException If the user's Operating System is not yet supported by this library
     */
    public void setSize(boolean development, @NotNull String width, @NotNull String height, @NotNull String pix_fmt)
            throws InvalidArgumentException, UnsupportedOperatingSystemException, UnsupportedOperationException {
        scalingParams.setSize(development, width, height, pix_fmt);
    }

    /**
     * This method sets the width and height of the resulting video by means of the given video size ID.
     * See <a href="https://ffmpeg.org/ffmpeg-all.html#Video-size">here</a> for the recognized size IDs.
     *
     * @param videoSizeID The given video size ID
     * @throws InvalidArgumentException if the given video size ID is null or not supported by FFmpeg
     * @throws UnsupportedOperationException If the video's width and height are already set
     */
    public void setVideoSizeID(@NotNull String videoSizeID) throws InvalidArgumentException, UnsupportedOperationException {
        scalingParams.setVideoSizeID(videoSizeID);
    }

    /**
     * Sets a new input color matrix with the given name.
     * @param name The name of the color matrix
     * @throws InvalidArgumentException If the color matrix's name is null, an empty string or a value not recognized by
     * FFmpeg
     */
    public void setInputColorMatrix(@NotNull String name) throws InvalidArgumentException {
        scalingParams.setInputColorMatrix(name);
    }

    /**
     * Sets a new input color matrix with the given name.
     * @param name The name of the color matrix
     * @throws InvalidArgumentException If the color matrix's name is null, an empty string or a value not recognized by
     * FFmpeg
     */
    public void setOutColorMatrix(@NotNull String name) throws InvalidArgumentException {
        scalingParams.setOutColorMatrix(name);
    }

    /**
     * Sets the input color range.
     * @param in_range The input color range
     * @throws InvalidArgumentException If the input color range is null, an empty string or not recognizable by FFmpeg
     */
    public void setInputRange(@NotNull String in_range) throws InvalidArgumentException {
        scalingParams.setInputRange(in_range);
    }

    /**
     * Sets the output color range.
     * @param out_range The output color range
     * @throws InvalidArgumentException If the output color range is null, an empty string or not recognizable by FFmpeg
     */
    public void setOutputRange(@NotNull String out_range) throws InvalidArgumentException {
        scalingParams.setOutputRange(out_range);
    }

    /**
     * Tells the program how to force the original aspect ratio.
     * @param val The parameter that tells the program how to force the original aspect ratio
     * @throws InvalidArgumentException If the parameter's value is null, an empty string or a value not recognized by
     * FFmpeg
     */
    public void forceOriginalAspectRatio(@NotNull String val) throws InvalidArgumentException {
        scalingParams.forceOriginalAspectRatio(val);
    }

    /**
     * Sets the integer by which the width and the height must be divisible.
     * @param val The given integer
     */
    public void setDivisibleBy(int val) {
        scalingParams.setDivisibleBy(val);
    }

    /**
     * Sets the width and height evaluation parameter.
     * @param eval The evaluation parameter
     * @throws InvalidArgumentException If the evaluation parameter is null, an empty string or a value different from
     * "init" and "frame"
     */
    public void setEval(@NotNull String eval) throws InvalidArgumentException {
        scalingParams.setEval(eval);
    }

    /**
     * Sets the interlacing mode.
     * @param val The given interlacing mode
     * @throws InvalidArgumentException If the given interlacing mode is null, an empty string or different from "1", "0"
     * or "-1"
     */
    public void setInterl(@NotNull String val) throws InvalidArgumentException {
        scalingParams.setInterl(val);
    }

    /**
     * Fills the Map declared in Filter with the correct values.
     */
    public void updateMap() {
        try {
            setOption("flags", scalingParams.getFlagsName());
            setOption("eval", scalingParams.getEval());
            setOption("interl", scalingParams.getInterl());

            setOption("size", scalingParams.getVideoSizeID());
            setOption("width", scalingParams.getWidth());
            setOption("height", scalingParams.getHeight());

            setOption("in_color_matrix", scalingParams.getInputColorMatrix());
            setOption("out_color_matrix", scalingParams.getOutputColorMatrix());

            setOption("in_range", scalingParams.getInputRange());
            setOption("out_range", scalingParams.getOutputRange());

            setOption("force_original_aspect_ratio", scalingParams.getForceOriginalAspectRatio());
            setOption("force_divisible_by", scalingParams.getDivisibleBy());
        } catch(InvalidArgumentException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
