package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale;

import it.disi.unitn.ProcessController;
import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import it.disi.unitn.exceptions.MultiLanguageUnsupportedOperationException;
import it.disi.unitn.videocreator.ExecutorResHandler;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.scalingalgs.ScalingAlgorithm;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;

/**
 * This class implements the "scale" video filter.
 */
public class Scale extends VideoFilter {

    private final ScalingParams scalingParams;

    private final Locale l;

    /**
     * This class's constructor. Constructs a new "scale" filter.
     */
    public Scale() {
        super("scale");
        scalingParams = new ScalingParams();
        l = Locale.getDefault();
    }

    /**
     * The class's constructor given pre-established scaling parameters.
     */
    public Scale(@NotNull ScalingAlgorithm sws_flags, @NotNull String eval, @NotNull String interl,
                 @NotNull String width, @NotNull String height, @NotNull String videoSizeID,
                 @NotNull String in_range, @NotNull String out_range, @NotNull String force_original_aspect_ratio,
                 @NotNull String inColMatrix, @NotNull String outColorMatrix, int force_divisible_by) {
        super("scale");

        if(sws_flags == null || checkNullOrEmpty(eval, interl, in_range, out_range,
                force_original_aspect_ratio, inColMatrix, outColorMatrix)) {
            printMsg("The scaling parameters arguments given to this constructor cannot be null.",
                    "L'istanza dei parametri di scaling fornita a questo costruttore non puo' essere null.");
            System.exit(1);
        }

        if(StringExt.checkNullOrEmpty(width) && StringExt.checkNullOrEmpty(height) && StringExt.checkNullOrEmpty(videoSizeID)) {
            printMsg("Either the frames' width and height or their size's ID must be set.",
                    "Almeno un parametro tra le dimensioni dei frame e l'ID della dimensione deve essere impostato.");
            System.exit(2);
        }

        if((StringExt.checkNullOrEmpty(width) && !StringExt.checkNullOrEmpty(height)) ||
                (!StringExt.checkNullOrEmpty(width) && StringExt.checkNullOrEmpty(height))) {
            printMsg("The width and height must be set at the same time.", "La larghezza e l'altezza devono " +
                    "essere impostate contemporaneamente.");
            System.exit(3);
        }

        if(!StringExt.checkNullOrEmpty(width) && !StringExt.checkNullOrEmpty(height) && !StringExt.checkNullOrEmpty(videoSizeID)) {
            printMsg("The frames' size and the size ID cannot be set at the same time.", "Le dimensioni dei " +
                    "frame e l'ID della dimensione non possono essere impostati contemporaneamente.");
            System.exit(4);
        }

        if(force_divisible_by <= 0) {
            printMsg("The force_divisible_by argument cannot be less than or equal to zero.", "L'argomento " +
                    "force_divisible_by non puo' essere minore o uguale a zero.");
            System.exit(5);
        }

        this.scalingParams = new ScalingParams(sws_flags, eval, interl, width, height, videoSizeID, in_range, out_range,
                force_original_aspect_ratio, inColMatrix, outColorMatrix, force_divisible_by);
        l = Locale.getDefault();
    }

    private static boolean checkNullOrEmpty(String ... args) {
        return Arrays.stream(args).anyMatch(StringExt::checkNullOrEmpty);
    }

    private void printMsg(@NotNull String msg, @NotNull String itmsg) {
        if(l == Locale.ITALIAN || l == Locale.ITALY) {
            System.err.println(itmsg);
        } else {
            System.err.println(msg);
        }
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
     * @param executor A DefaultExecutor instance.
     * @param outstream An OutputStream instance.
     * @param tempp A temporary file on which the user has writing permissions.
     * @param cmdline A CommandLine instance
     * @return True if the CommandLine instance has a field "value" whose value is equal to zero, otherwise false
     */
    private boolean executeCommand(@NotNull DefaultExecutor executor, @NotNull OutputStream outstream, @NotNull Path tempp,
                                   @NotNull CommandLine cmdline) {
        try {
            ProcessController controller = new ProcessController(executor, outstream, tempp);
            if (controller.execute(cmdline) == 0) {
                printMsg("At least one of the given values was not recognised by FFmpeg.",
                        "Almeno uno dei valori forniti non e' stato riconosciuto da FFmpeg.");
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
     * @throws MultiLanguageUnsupportedOperationException If the video's size id is already set
     * @throws UnsupportedOperatingSystemException If the user's Operating System is not yet supported by this library
     */
    public void setSize(boolean development, @NotNull String width, @NotNull String height, @NotNull String pix_fmt)
            throws InvalidArgumentException, UnsupportedOperatingSystemException, MultiLanguageUnsupportedOperationException {
        scalingParams.setSize(development, width, height, pix_fmt);
    }

    /**
     * This method sets the width and height of the resulting video by means of the given video size ID.
     * See <a href="https://ffmpeg.org/ffmpeg-all.html#Video-size">here</a> for the recognized size IDs.
     *
     * @param videoSizeID The given video size ID
     * @throws InvalidArgumentException if the given video size ID is null or not supported by FFmpeg
     * @throws MultiLanguageUnsupportedOperationException If the video's width and height are already set
     */
    public void setVideoSizeID(@NotNull String videoSizeID) throws InvalidArgumentException, MultiLanguageUnsupportedOperationException {
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
            setOption("force_divisible_by", String.valueOf(scalingParams.getDivisibleBy()));
        } catch(InvalidArgumentException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
