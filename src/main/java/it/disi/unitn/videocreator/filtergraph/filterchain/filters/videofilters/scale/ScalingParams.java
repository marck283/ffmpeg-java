package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale;

import it.disi.unitn.ProcessController;
import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import it.disi.unitn.exceptions.UnsupportedOperationException;
import it.disi.unitn.videocreator.ExecutorResHandler;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.size.Size;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.scalingalgs.ScalingAlgorithm;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;
import org.owasp.encoder.Encode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class ScalingParams {

    private ScalingAlgorithm sws_flags; //These flags will then be joined by using String.join("+", sws_flags)

    private String width, height, videoSizeID;

    private String eval, interl, in_range, out_range, force_original_aspect_ratio;

    private ColorMatrix inColMatrix, outColorMatrix;

    private int force_divisible_by;

    ScalingParams() {
        eval = "init";
        interl = "0";
        width = "";
        height = "";
        force_divisible_by = 1;
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
     * This method returns the name of the chosen scaling algorithm, or an empty string if no algorithm was chosen.
     * @return The name of the chosen scaling algorithm
     */
    public String getFlagsName() {
        return (sws_flags == null) ? "" : sws_flags.getName();
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
                Locale l = Locale.getDefault();
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
     * This method checks if the given dimensions are accepted by FFmpeg.
     *
     * @param width   The width
     * @param height  The height
     * @param pix_fmt The pixel format used in the video
     * @return True if the given dimensions are accepted, false otherwise
     * @throws UnsupportedOperatingSystemException If the Operating System the user is currently operating on is not yet
     *                                             supported by this library
     */
    private boolean checkSize(@NotNull String width, @NotNull String height, @NotNull String pix_fmt) throws UnsupportedOperatingSystemException {
        CommandLine cmdLine;
        Map<String, String> m = new HashMap<>();
        m.put("width", width);
        m.put("height", height);
        pix_fmt = Encode.forJava(pix_fmt);
        m.put("pix_fmt", pix_fmt);
        if (SystemUtils.IS_OS_LINUX) {
            cmdLine = CommandLine.parse("./src/ffcodec/bin/linux/checkSize/main ${width} ${height} ${pix_fmt}", m);
        } else {
            if (SystemUtils.IS_OS_WINDOWS) {
                cmdLine = CommandLine.parse("./src/ffcodec/bin/windows/checkSize.exe ${width} ${height} ${pix_fmt}",
                        m);
            } else {
                throw new UnsupportedOperatingSystemException();
            }
        }
        PumpStreamHandler streamHandler = new PumpStreamHandler();
        DefaultExecutor executor = DefaultExecutor.builder().get();
        executor.setStreamHandler(streamHandler);
        ExecutorResHandler execResHandler = new ExecutorResHandler();
        return executeCommand(executor, execResHandler, cmdLine);
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
            throws InvalidArgumentException, UnsupportedOperatingSystemException, it.disi.unitn.exceptions.UnsupportedOperationException {
        if(!StringExt.checkNullOrEmpty(videoSizeID)) {
            it.disi.unitn.exceptions.UnsupportedOperationException.throwUnsupportedOperationException("Cannot set video width and height if the " +
                    "video's size id is already set.", "Non e' possibile impostare l'ampiezza e l'altezza del video " +
                    "quando l'id della sua dimensione e' gia' stato impostato.");
        }

        if(StringExt.checkNullOrEmpty(width)) {
            throw new InvalidArgumentException("The video width cannot be null or an empty string.", "L'ampiezza dell'immagine " +
                    "non puo' essere null o una stringa vuota.");
        }

        if(StringExt.checkNullOrEmpty(height)) {
            throw new InvalidArgumentException("The video height cannot be null or an empty string.", "L'altezza dell'immagine " +
                    "non puo' essere null o una stringa vuota.");
        }

        /*try {
            int w = Integer.parseInt(width), h = Integer.parseInt(height);
            if(w <= 0 || h <= 0) {
                throw new InvalidArgumentException("The given width or height cannot be negative or null numbers.",
                        "L'ampiezza e l' altezza non possono essere numeri negativi o nulli.");
            }
        } catch(NumberFormatException ex) {
            throw new InvalidArgumentException("Either the width or the height is not an integer.", "L'ampiezza o l'altezza " +
                    "non rappresentano un numero intero.");
        }*/

        if(StringExt.checkNullOrEmpty(pix_fmt)) {
            throw new InvalidArgumentException("The pixel format must neither be null nor an empty string.",
                    "Il formato dei pixel non puo' essere null o una stringa vuota.");
        }

        if (!development || checkSize(width, height, pix_fmt)) {
            this.width = width;
            this.height = height;
        }
    }

    /**
     * This method returns the filter's width, or an empty string if the width is null or an empty string itself.
     * @return The filter's width, or an empty string if the width is null or an empty string itself
     */
    public String getWidth() {
        return (StringExt.checkNullOrEmpty(width)) ? "" : width;
    }

    /**
     * This method returns the filter's height, or an empty string if the height is null or an empty string itself.
     * @return The filter's height, or an empty string if the height is null or an empty string itself
     */
    public String getHeight() {
        return (StringExt.checkNullOrEmpty(height)) ? "" : height;
    }

    /**
     * This method sets the width and height of the resulting video by means of the given video size ID.
     * See <a href="https://ffmpeg.org/ffmpeg-all.html#Video-size">here</a> for the recognized size IDs.
     *
     * @param videoSizeID The given video size ID
     * @throws InvalidArgumentException if the given video size ID is null or not supported by FFmpeg
     * @throws it.disi.unitn.exceptions.UnsupportedOperationException If the video's width and height are already set
     */
    public void setVideoSizeID(@NotNull String videoSizeID) throws InvalidArgumentException, it.disi.unitn.exceptions.UnsupportedOperationException {
        if(!StringExt.checkNullOrEmpty(width) && !StringExt.checkNullOrEmpty(height)) {
            UnsupportedOperationException.throwUnsupportedOperationException("Cannot set video size id if width and " +
                    "height are already set.", "Impossibile impostare l'id della dimensione del video quando l'ampiezza " +
                    "e l'altezza sono gia' impostate.");
        }
        if (StringExt.checkNullOrEmpty(videoSizeID)) {
            throw new InvalidArgumentException("The video size ID should not be null.", "L'ID della proporzione visiva " +
                    "non deve essere null.");
        }

        Size size = new Size();
        if (size.checkSizeID(videoSizeID)) {
            this.videoSizeID = videoSizeID;
        } else {
            throw new InvalidArgumentException("Invalid image resolution", "Risoluzione immagine non valida.");
        }
    }

    /**
     * This method returns the filter's video size ID, or an empty string if the video size ID is null or an empty string
     * itself.
     * @return The filter's video size ID, or an empty string if the video size ID is null or an empty string itself
     */
    public String getVideoSizeID() {
        return (StringExt.checkNullOrEmpty(videoSizeID)) ? "" : videoSizeID;
    }

    /**
     * Sets a new input color matrix with the given name.
     * @param name The name of the color matrix
     * @throws InvalidArgumentException If the color matrix's name is null, an empty string or a value not recognized by
     * FFmpeg
     */
    public void setInputColorMatrix(@NotNull String name) throws InvalidArgumentException {
        inColMatrix = new ColorMatrix(name);
    }

    /**
     * Sets a new input color matrix with the given name.
     * @param name The name of the color matrix
     * @throws InvalidArgumentException If the color matrix's name is null, an empty string or a value not recognized by
     * FFmpeg
     */
    public void setOutColorMatrix(@NotNull String name) throws InvalidArgumentException {
        outColorMatrix = new ColorMatrix(name);
    }

    /**
     * Sets the input color range.
     * @param in_range The input color range
     * @throws InvalidArgumentException If the input color range is null, an empty string or not recognizable by FFmpeg
     */
    public void setInputRange(@NotNull String in_range) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(in_range)) {
            throw new InvalidArgumentException("The input color range cannot be null or an empty string.", "Il range di " +
                    "input dei colori non puo' essere null o una stringa vuota.");
        }

        switch(in_range) {
            case "auto", "unknown", "jpeg", "full", "pc", "mpeg", "limited", "tv" -> this.in_range = in_range;
            default -> throw new InvalidArgumentException("The input color range must be equal to \"auto/unknown\", " +
                    "\"jpeg/full/pc\" or \"mpeg/limited/tv\".", "L'intervallo dei colori in input deve essere uguale a " +
                    "\"auto/unknown\", \"jpeg/full/pc\" o \"mpeg/limited/tv\".");
        }
    }

    /**
     * Sets the output color range.
     * @param out_range The output color range
     * @throws InvalidArgumentException If the output color range is null, an empty string or not recognizable by FFmpeg
     */
    public void setOutputRange(@NotNull String out_range) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(out_range)) {
            throw new InvalidArgumentException("The input color range cannot be null or an empty string.", "Il range di " +
                    "input dei colori non puo' essere null o una stringa vuota.");
        }

        switch(out_range) {
            case "auto", "unknown", "jpeg", "full", "pc", "mpeg", "limited", "tv" -> this.out_range = out_range;
            default -> throw new InvalidArgumentException("The input color range must be equal to \"auto/unknown\", " +
                    "\"jpeg/full/pc\" or \"mpeg/limited/tv\".", "L'intervallo dei colori in input deve essere uguale a " +
                    "\"auto/unknown\", \"jpeg/full/pc\" o \"mpeg/limited/tv\".");
        }
    }

    /**
     * Tells the program how to force the original aspect ratio.
     * @param val The parameter that tells the program how to force the original aspect ratio
     * @throws InvalidArgumentException If the parameter's value is null, an empty string or a value not recognized by
     * FFmpeg
     */
    public void forceOriginalAspectRatio(@NotNull String val) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(val)) {
            throw new InvalidArgumentException("The parameter given to this method cannot be null or an empty string.",
                    "Il parametro fornito a questo metodo non puo' essere null o una stringa vuota.");
        }

        switch(val) {
            case "disable", "decrease", "increase" -> force_original_aspect_ratio = val;
            default -> throw new InvalidArgumentException("The parameter given to this method must have a value recognizable " +
                    "by FFmpeg.", "Il parametro fornito a questo metodo deve avere un valore riconoscibile da FFmpeg.");
        }
    }

    /**
     * Sets the integer by which the width and the height must be divisible.
     * @param val The given integer
     */
    public void setDivisibleBy(int val) {
        if(val > 0) {
            force_divisible_by = val;
        }
    }

    public String getDivisibleBy() {
        return String.valueOf(force_divisible_by);
    }

    /**
     * Sets the width and height evaluation parameter.
     * @param eval The evaluation parameter
     * @throws InvalidArgumentException If the evaluation parameter is null, an empty string or a value different from
     * "init" and "frame"
     */
    public void setEval(@NotNull String eval) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(eval)) {
            throw new InvalidArgumentException("The width and height's evaluation parameter cannot be null or an empty " +
                    "string.", "Il valore del parametro di valutazione dell'ampiezza e dell'altezza non puo' essere null " +
                    "o una stringa vuota.");
        }

        switch(eval) {
            case "eval", "init" -> this.eval = eval;
            default -> throw new InvalidArgumentException("The width and height's evaluation parameter's value must be " +
                    "equal to \"eval\" or \"init\".", "Il valore del parametro di valutazione dell'ampiezza e dell'altezza " +
                    "deve essere uguale a \"eval\" o \"init\".");
        }
    }

    /**
     * This method returns the value of the "eval" option.
     * @return The value of the "eval" option
     */
    public String getEval() {
        return eval;
    }

    /**
     * Sets the interlacing mode.
     * @param val The given interlacing mode
     * @throws InvalidArgumentException If the given interlacing mode is null, an empty string or different from "1", "0"
     * or "-1"
     */
    public void setInterl(@NotNull String val) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(val)) {
            throw new InvalidArgumentException("The interlacing mode cannot be null or an empty string.", "La modalita' " +
                    "di interlacciamento non puo' essere null o unstringa vuota.");
        }

        interl = val;
    }

    /**
     * This method returns the value of the "interl" option.
     * @return The value of the "interl" option
     */
    public String getInterl() {
        return interl;
    }

}
