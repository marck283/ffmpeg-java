package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import it.disi.unitn.videocreator.ExecutorResHandler;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;
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

/**
 * This class implements the "scale" video filter.
 */
public class Scale extends VideoFilter {

    private ScalingAlgorithm sws_flags; //These flags will then be joined by using String.join("+", sws_flags)

    private String width, height, videoSizeID;

    private String eval = "init", interl = "0", in_range, out_range, force_original_aspect_ratio;

    private ColorMatrix inColMatrix, outColorMatrix;

    private int force_divisible_by;

    /**
     * This class's constructor. Constructs a new "scale" filter.
     * @throws InvalidArgumentException If the filter's name (given by this constructor to the superclass) is null or
     * an empty string
     */
    public Scale() throws InvalidArgumentException {
        super("scale");
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
     * This method executes the given Command Line command.
     *
     * @param executor       A DefaultExecutor instance
     * @param execResHandler An ExecutorResHandler instance
     * @param cmdline        A CommandLine instance
     * @return True if the CommandLine instance has a field "value" whose value is equal to zero, otherwise false
     */
    private boolean executeCML(@NotNull DefaultExecutor executor, @NotNull ExecutorResHandler execResHandler,
                               @NotNull CommandLine cmdline) {
        try {
            executor.execute(cmdline, execResHandler);
            Thread t1 = new Thread(() -> {
                try {
                    execResHandler.doWait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            t1.start();
            t1.join();

            int val = execResHandler.getValue();
            execResHandler.setValue(0);

            Locale l = Locale.getDefault();
            if (val == 0) {
                if (l == Locale.ITALY || l == Locale.ITALIAN) {
                    System.err.println("Questo codec non e' supportato dall'installazione di FFmpeg presente in questo sistema. " +
                            "Si prega di riprovare con un altro codec.");
                } else {
                    System.err.println("This codec is not supported by your installation of FFmpeg. Please try another one.");
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
     * @throws NotEnoughArgumentsException         If the pixel format is null or an empty string
     * @throws UnsupportedOperatingSystemException If the Operating System the user is currently operating on is not yet
     *                                             supported by this library
     */
    private boolean checkSize(@NotNull String width, @NotNull String height, @NotNull String pix_fmt) throws NotEnoughArgumentsException, UnsupportedOperatingSystemException {
        if (pix_fmt == null || pix_fmt.isEmpty()) {
            throw new NotEnoughArgumentsException("The pixel format must neither be null nor an empty string.",
                    "Il formato dei pixel non puo' essere null o una stringa vuota.");
        }
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
        return executeCML(executor, execResHandler, cmdLine);
    }

    /**
     * This method sets the width and height of the video.
     * @param width The video's width
     * @param height The video's height
     * @throws InvalidArgumentException If the video's with or height is null or an empty string
     * @throws UnsupportedOperationException If the video's size id is already set
     */
    public void setSize(boolean development, @NotNull String width, @NotNull String height, @NotNull String pix_fmt)
            throws InvalidArgumentException, UnsupportedOperationException, UnsupportedOperatingSystemException, NotEnoughArgumentsException {
        if(videoSizeID != null && !videoSizeID.isEmpty()) {
            Locale l = Locale.getDefault();
            if(l == Locale.ITALIAN || l == Locale.ITALY) {
                System.err.println("Non e' possibile impostare l'ampiezza e l'altezza del video quando l'id della sua " +
                        "dimensione e' gia' stato impostato.");
            } else {
                System.err.println("Cannot set video width and height if the video's size id is already set.");
            }
            throw new UnsupportedOperationException();
        }

        if(checkNullOrEmpty(width)) {
            throw new InvalidArgumentException("The video width cannot be null or an empty string.", "L'ampiezza dell'immagine " +
                    "non puo' essere null o una stringa vuota.");
        }

        if(checkNullOrEmpty(height)) {
            throw new InvalidArgumentException("The video height cannot be null or an empty string.", "L'altezza dell'immagine " +
                    "non puo' essere null o una stringa vuota.");
        }

        if (!development || checkSize(width, height, pix_fmt)) {
            this.width = width;
            this.height = height;
        }
    }

    /**
     * This method checks if the given size ID is accepted by FFmpeg.
     *
     * @param sizeID The given size ID
     * @return True if the given size ID is accepted by FFmpeg, otherwise false
     */
    private boolean checkSizeID(@NotNull String sizeID) {
        return switch (sizeID) {
            case "ntsc", "pal", "qntsc", "qpal", "sntsc", "spal", "film", "ntsc-film", "sqcif", "qcif", "cif", "4cif",
                 "16cif", "qqvga", "qvga", "vga", "svga", "xga", "uxga", "qxga", "sxga", "qsxga", "hsxga", "wvga",
                 "wxga", "wsxga", "wuxga", "woxga", "wqsxga", "wquxga", "whsxga", "whuxga", "cga", "ega", "hd480",
                 "hd720", "hd1080", "2k", "2kflat", "2kscope", "4k", "4kflat", "4kscope", "nhd", "hqvga", "wqvga",
                 "fwqvga", "hvga", "qhd", "2kdci", "4kdci", "uhd2160", "uhd4320" -> true;
            default -> false;
        };
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
        if(width != null && !width.isEmpty() && height != null && !height.isEmpty()) {
            Locale l = Locale.getDefault();
            if(l == Locale.ITALIAN || l == Locale.ITALY) {
                System.err.println("Impossibile impostare l'id della dimensione del video quando l'ampiezza e l'altezza " +
                        "sono gia' impostate.");
            } else {
                System.err.println("Cannot set video size id if width and height are already set.");
            }
            throw new UnsupportedOperationException();
        }
        if (videoSizeID == null) {
            throw new InvalidArgumentException("The video size ID should not be null.", "L'ID della proporzione visiva " +
                    "non deve essere null.");
        }
        if (checkSizeID(videoSizeID)) {
            this.videoSizeID = videoSizeID;
        } else {
            throw new InvalidArgumentException("Invalid image resolution", "Risoluzione immagine non valida.");
        }
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
        if(in_range == null || in_range.isEmpty()) {
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
        if(out_range == null || out_range.isEmpty()) {
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
        if(val == null || val.isEmpty()) {
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

    /**
     * Sets the width and height evaluation parameter.
     * @param eval The evaluation parameter
     * @throws InvalidArgumentException If the evaluation parameter is null, an empty string or a value different from
     * "init" and "frame"
     */
    public void setEval(@NotNull String eval) throws InvalidArgumentException {
        if(eval == null || eval.isEmpty()) {
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
     * Sets the interlacing mode.
     * @param val The given interlacing mode
     * @throws InvalidArgumentException If the given interlacing mode is null, an empty string or different from "1", "0"
     * or "-1"
     */
    public void setInterl(@NotNull String val) throws InvalidArgumentException {
        if(val == null || val.isEmpty()) {
            throw new InvalidArgumentException("The interlacing mode cannot be null or an empty string.", "La modalita' " +
                    "di interlacciamento non puo' essere null o unstringa vuota.");
        }

        interl = val;
    }

    /**
     * Fills the Map declared in Filter with the correct values.
     */
    public void updateMap() {
        if(sws_flags != null) {
            options.put("flags", sws_flags.toString());
        }
        options.put("eval", eval);
        options.put("interl", interl);
        if(videoSizeID != null && !videoSizeID.isEmpty()) {
            options.put("size", videoSizeID);
        } else {
            options.put("width", width);
            options.put("height", height);
        }
        if(inColMatrix == null) {
            options.put("in_color_matrix", "auto");
        } else {
            options.put("in_color_matrix", inColMatrix.getName());
        }
        if(outColorMatrix == null) {
            options.put("out_color_matrix", "auto");
        } else {
            options.put("out_color_matrix", outColorMatrix.getName());
        }
        if(in_range == null || in_range.isEmpty()) {
            options.put("in_range", "auto");
        } else {
            options.put("in_range", in_range);
        }
        if(out_range == null || out_range.isEmpty()) {
            options.put("out_range", "auto");
        } else {
            options.put("out_range", out_range);
        }
        if(force_original_aspect_ratio == null || force_original_aspect_ratio.isEmpty()) {
            options.put("force_original_aspect_ratio", "disable");
        } else {
            options.put("force_original_aspect_ratio", force_original_aspect_ratio);
        }
        if(force_divisible_by > 0) {
            options.put("force_divisible_by", String.valueOf(force_divisible_by));
        }
    }
}
