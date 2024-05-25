package it.disi.unitn.videocreator;

import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.ProcessController;
import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import it.disi.unitn.exceptions.PermissionsException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import it.disi.unitn.videocreator.filtergraph.AudioFilterGraph;
import it.disi.unitn.videocreator.filtergraph.FilterGraph;
import it.disi.unitn.videocreator.filtergraph.VideoFilterGraph;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.format.Format;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.Scale;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.scalingalgs.ScalingAlgorithm;
import it.disi.unitn.videocreator.fps_mode.FPSMode;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * This class is used to create videos using different options compatible with ffmpeg.
 */
public class VideoCreator {

    private final FFMpegBuilder builder;

    private int frameRate = 1, videoDuration; //Frame rate and video duration

    private final String outputFile; //Name of output file

    private String codecID = "h264", audioCodec; //Name of video and audio codec ID

    //Pattern of the names of the files to be included in the video or name of the file to be included in the video
    private final List<String> pattern;

    private int videoQuality = 0; //Quality of output video. For "x264", sane values should be between 18 and 28

    private int startInstant; //Starting instant of the new video with respect to the original video

    private String audioBitRate; //Audio track's bitrate (in Kbit/s)

    private String videoBitRate; //Video track's bitrate (in Kbit/s or Mbit/s)

    private String pixelFormat, execFile = "";

    private boolean /*isOutFullRange, */videoStreamCopy, audioStreamCopy;

    private final Locale l;

    private FilterGraph vfg, afg, cfg; //Video, audio and complex filter graphs

    private FPSMode fps_mode;

    /**
     * The constructor of this class.
     *
     * @param builder    The FFMpegBuilder instance that called this constructor
     * @param outputFile The path to the output file
     * @throws NotEnoughArgumentsException if any of the arguments given to this constructor is null
     */
    public VideoCreator(@NotNull FFMpegBuilder builder, @NotNull String outputFile) throws NotEnoughArgumentsException {
        if (builder == null || StringExt.checkNullOrEmpty(outputFile)) {
            throw new NotEnoughArgumentsException("The arguments given to this class's constructor cannot be null or " +
                    "empty values.", "Gli argomenti forniti al costruttore di questa classe non possono essere null o " +
                    "valori non specificati.");
        } else {
            this.pattern = new ArrayList<>();
            this.builder = builder;
            this.outputFile = outputFile;
            if (SystemUtils.IS_OS_LINUX) {
                execFile = "./src/ffcodec/bin/linux/ffcodec";
            } else {
                if (SystemUtils.IS_OS_WINDOWS) {
                    execFile = "./src/ffcodec/bin/windows/ffcodec.exe";
                }
            }
            File file = new File(execFile);
            if (!file.exists()) {
                System.err.println("ffcodec does not exist");
                System.exit(1);
            }
            //isOutFullRange = false;
            l = Locale.getDefault();
        }
    }

    /**
     * Adds an input file.
     *
     * @param input The given input file
     * @throws InvalidArgumentException If the given input value is null or an empty string or the given file does not
     *                                  exist
     */
    public void addInput(@NotNull String input) throws InvalidArgumentException {
        if (StringExt.checkNullOrEmpty(input)) {
            throw new InvalidArgumentException("The given input file cannot be null or an empty string.", "Il file fornito " +
                    "non puo' essere null o una stringa vuota.");
        }

        pattern.forEach(e -> {
            if (e.contains("*.")) {
                if (l == Locale.ITALY || l == Locale.ITALIAN) {
                    System.err.println("Non e' possibile inserire un altro valore di input quando ne e' gia' presente uno " +
                            "che comprenda tutti i file presenti nella cartella.");
                } else {
                    System.err.println("Cannot insert another input value when there is already another one that includes " +
                            "all files in the same folder.");
                }
                throw new UnsupportedOperationException();
            }
        });

        /*if(input.startsWith("*.") && !pattern.isEmpty()) {

        }*/

        if (!input.startsWith("*.") && !Files.exists(Paths.get(input))) {
            throw new InvalidArgumentException("The given file does not exist.", "Il file fornito non esiste.");
        }

        pattern.add(input);
    }

    /**
     * This method sets the output video's frame rate.
     *
     * @param val The output video's frame rate
     * @throws InvalidArgumentException when the given frame rate is less than or equal to 0.
     */
    public void setFrameRate(int val) throws InvalidArgumentException {
        if (val <= 0) {
            throw new InvalidArgumentException("The frame rate value should always be greater than or equal to 1.",
                    "Il valore del frame rate deve essere sempre maggiore o uguale ad 1.");
        } else {
            frameRate = val;
        }
    }

    /**
     * This method sets the duration of the output video.
     *
     * @param val The duration of the output video
     * @throws InvalidArgumentException when the given duration is less than or equal to 0.
     */
    public void setVideoDuration(int val) throws InvalidArgumentException {
        if (val <= 0) {
            throw new InvalidArgumentException("The video duration must always be of at least 1 second.", "La durata del " +
                    "video deve essere di almeno 1 secondo.");
        }
        videoDuration = val;
    }

    /**
     * This method sets the starting instant of the resulting video, but it requires that the input file be itself a video
     * of any format supported by ffmpeg.
     *
     * @param val The starting instant of the new video
     * @throws InvalidArgumentException when the starting instant of the new video is less than or equal to 0.
     */
    public void setStartInstant(int val) throws InvalidArgumentException {
        if (val <= 0) {
            throw new InvalidArgumentException("The starting instant of the new video must be at least the starting instant" +
                    " of the original video.", "L'istante di partenza del nuovo video deve essere almeno pari a quello" +
                    " del video originale.");
        }
        startInstant = val;
    }

    /**
     * This method checks if the given file path refers to an executable file.
     *
     * @return True if thr path refers to an executable file, otherwise false.
     */
    private boolean checkExecutable() throws PermissionsException {
        if (!Files.isExecutable(Paths.get(execFile))) {
            try {
                throw new PermissionsException("Cannot execute file " + execFile + ". Please check the user's permissions and that " +
                        "the file exists.", "Impossibile eseguire il file " + execFile + ". Si prega di controllarne i permessi " +
                        "di esecuzione e l'esistenza.");
            } catch(InvalidArgumentException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }

        return true;
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
            ProcessController controller = new ProcessController(executor, execResHandler);
            int val = controller.execute(cmdline);
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
     * This method checks if the given codec is supported by the user's installation of FFmpeg.
     *
     * @param cmdline The CommandLine instance
     * @return True if the user's installation of FFmpeg supports the given codec, otherwise false
     * @throws IOException If an I/O error occurs
     */
    private boolean performCheck(@NotNull CommandLine cmdline) throws IOException {
        try {
            if (!checkExecutable()) {
                return false;
            }

            Path tempFile = Files.createTempFile("ffmpeg-java-temp", ".txt");
            BufferedOutputStream outstream = new BufferedOutputStream(Files.newOutputStream(tempFile,
                    StandardOpenOption.WRITE));
            PumpStreamHandler streamHandler = new PumpStreamHandler(outstream, System.err);
            DefaultExecutor executor = DefaultExecutor.builder().get();
            executor.setStreamHandler(streamHandler);
            ExecutorResHandler execResHandler = new ExecutorResHandler(outstream, tempFile);
            return executeCML(executor, execResHandler, cmdline);
        } catch (InvalidPathException ex) {
            System.err.println(ex.getLocalizedMessage());
        }

        return false;
    }

    /**
     * This method checks if the given codec is supported by the current installation of FFmpeg.
     *
     * @param s The given codec ID.
     * @return true if the given codec is supported, otherwise false
     * @throws IOException                         If an I/O error occurs
     * @throws UnsupportedOperatingSystemException if the Operating System is not yet supported.
     */
    private boolean enumCodecs(String s) throws IOException, UnsupportedOperatingSystemException {
        if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_WINDOWS) {
            Map<String, String> m = new HashMap<>();
            m.put("execFile", execFile);
            m.put("s", s);
            return performCheck(CommandLine.parse("${execFile} ${s}", m));
        } else {
            throw new UnsupportedOperatingSystemException();
        }
    }

    /**
     * This method sets the codec ID of the output video.
     *
     * @param codecID     The given codec ID
     * @param development This boolean field tells the method if th user is running the program with a recompiled version
     *                    of FFmpeg
     * @throws NotEnoughArgumentsException         if the given codec ID is null
     * @throws InvalidArgumentException            if the given codec ID is not supported by ffmpeg
     * @throws IOException                         if an I/O error occurs
     * @throws UnsupportedOperatingSystemException if the Operating System is not yet supported
     */
    public void setCodecID(@NotNull String codecID, boolean development) throws NotEnoughArgumentsException, InvalidArgumentException,
            IOException, UnsupportedOperatingSystemException {
        if (StringExt.checkNullOrEmpty(codecID)) {
            throw new NotEnoughArgumentsException("The codec id must not be null.", "L'id del codec non deve essere null " +
                    "o una stringa vuota.");
        }
        if (!development) {
            this.codecID = codecID;
            return;
        }
        if (enumCodecs(codecID)) {
            this.codecID = codecID;
            if (codecID.equals("h264")) {
                setPixelFormat("yuv420p");
            }
            if (codecID.equals("mjpeg")) {
                setPixelFormat("yuvj420p");
            }
        } else {
            throw new InvalidArgumentException("Invalid video codec.", "Codec video non valido.");
        }
    }

    /**
     * This method allows the user to set the audio codec for the resulting video. WARNING: the audio codec should always
     * be compatible with the video codec.
     *
     * @param ac The audio codec to be used in the resulting video
     * @throws InvalidArgumentException If the given audio codec is represented by a nul or empty string
     */
    public void setAudioCodec(@NotNull String ac) throws InvalidArgumentException {
        if (StringExt.checkNullOrEmpty(ac)) {
            throw new InvalidArgumentException("The given audio codec is represented by a null or empty string.", "Il " +
                    "codec audio fornito e' rappresentato da una stringa null o vuota.");
        }
        audioCodec = ac;
    }

    /*/**
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
    /*private boolean checkSize(int width, int height, @NotNull String pix_fmt) throws NotEnoughArgumentsException, UnsupportedOperatingSystemException {
        if (pix_fmt == null || pix_fmt.isEmpty()) {
            throw new NotEnoughArgumentsException("The pixel format must neither be null nor an empty string.",
                    "Il formato dei pixel non puo' essere null o una stringa vuota.");
        }
        CommandLine cmdLine;
        Map<String, String> m = new HashMap<>();
        m.put("width", String.valueOf(width));
        m.put("height", String.valueOf(height));
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
    }*/

    /**
     * This method sets the quality of the output video.
     *
     * @param quality The quality of the output video
     */
    public void setVideoQuality(int quality) {
        videoQuality = quality;
    }

    /*/**
     * This method sets the size of the resulting video by means of the given width and height.
     *
     * @param width       The given width
     * @param height      The given height
     * @param pix_fmt     The pixel format used in the resulting video
     * @param development A boolean parameter indicating whether the user is running the program with a custom version
     *                    of FFmpeg
     * @throws InvalidArgumentException            if the given width or height parameter is less than or equal to 0
     * @throws NotEnoughArgumentsException         If the given pixel format is null or an empty string
     * @throws UnsupportedOperatingSystemException If the Operating System the user is currently operating on is not yet
     *                                             supported by this library
     */
    /*public void setVideoSize(int width, int height, @NotNull String pix_fmt, boolean development) throws InvalidArgumentException,
            NotEnoughArgumentsException, UnsupportedOperatingSystemException {
        if (width <= 0 || height <= 0) {
            throw new InvalidArgumentException("The given width or height parameter must be a strictly positive " +
                    "integer value.", "Sia l'ampiezza che la larghezza devono essere valori strettamente positivi.");
        }

        if (!development || checkSize(width, height, pix_fmt)) {
            videoWidth = width;
            videoHeight = height;
        }
    }*/

    /*/**
     * Sets the video size id.
     * @param videoSizeID The given video size id
     * @throws InvalidArgumentException If the given video size id is null or an empty string
     */
    /*public void setVideoSizeID(@NotNull String videoSizeID) throws InvalidArgumentException {
        if(videoSizeID == null || videoSizeID.isEmpty()) {
            throw new InvalidArgumentException("The video size ID cannot be null or an empty string.", "L'id della dimensione " +
                    "del video non puo' essere null o una stringa vuota.");
        }

        this.videoSizeID = videoSizeID;
    }*/

    /**
     * This method sets the audio track&rsquo;s bitrate (in Kbit/s).
     *
     * @param val The audio track&rsquo;s bitrate
     * @throws InvalidArgumentException of the given bitrate value is less than or equal to 0
     */
    public void setAudioBitRate(int val) throws InvalidArgumentException {
        if (val <= 0) {
            throw new InvalidArgumentException("The bitrate of the audio track must be greater than 0.", "Il bitrate " +
                    "della traccia audio deve essere maggiore di 0.");
        }
        audioBitRate = val + "k";
    }

    /**
     * This method sets the output video&rsquo;s bitrate in Kilobits or Megabits.
     *
     * @param val  The bitrate value
     * @param mode A value between "k" and "m" (short for Kilobit and Megabit respectively)
     * @throws InvalidArgumentException If the "mode" argument is null, or it is not equal to "k" or "m"
     */
    public void setVideoBitRate(int val, @NotNull String mode) throws InvalidArgumentException {
        if (StringExt.checkNullOrEmpty(mode) || (!mode.equals("k") && !mode.equals("m"))) {
            throw new InvalidArgumentException("The \"mode\" parameter must be specified and it must be either \"k\" " +
                    "or \"m\".", "Il parametro \"mode\" deve essere specificato e deve avere valore pari a \"k\" o " +
                    "\"m\".");
        }
        videoBitRate = val + mode;
    }

    /**
     * Sets the pixel format.
     *
     * @param pxfmt The identifier of the requested pixel format
     * @throws InvalidArgumentException If the given pixel format is null
     */
    public void setPixelFormat(@NotNull String pxfmt) throws InvalidArgumentException {
        if (StringExt.checkNullOrEmpty(pxfmt)) {
            throw new InvalidArgumentException("The argument to this method cannot be null or an empty string.", "L'argomento " +
                    "fornito a questo metodo non puo' essere null o una stringa vuota.");
        }
        pixelFormat = pxfmt;
    }

    /*/**
     * Questo metodo imposta la modalit&agrave; di resa dei colori.
     *
     * @param val Valore booleano per indicare la modalit&agrave; di resa dei colori
     */
    /*public void setOutFullRange(boolean val) {
        isOutFullRange = val;
    }*/

    /**
     * Enables/disables video stream copying.
     *
     * @param streamCopy Boolean parameter to enable/disable video stream copying
     */
    public void setVideoStreamCopy(boolean streamCopy) {
        videoStreamCopy = streamCopy;
    }

    /**
     * Enables/disables audio stream copying.
     *
     * @param streamCopy Boolean parameter to enable/disable audio stream copying
     */
    public void setAudioStreamCopy(boolean streamCopy) {
        audioStreamCopy = streamCopy;
    }

    /**
     * Sets the "scale" filter parameters.
     *
     * @param development   A boolean parameter to tell the program if the user is running a custom version of FFmpeg
     * @param scale         The "scale" filter instance
     * @param alg           The ScalingAlgorithm instance
     * @param width         Each frame's width
     * @param height        Each frame's height
     * @param incolmatname  The name of the input color matrix, as described by FFmpeg's documentation of the scaling filter
     * @param outcolmatname The name of the output color matrix, as described by FFmpeg's documentation of the scaling
     *                      filter
     * @param incolrange    The input color range
     * @param outcolrange   The output color range
     * @param evalSize      The value that tells when to evaluate the expressions for width and height
     * @param interlMode    The interlacing mode
     * @param forceOAsRatio A parameter that tells the program whether to force the original aspect ratio
     * @param divisibleBy   An integer that tells the program what the width and height should be divisible by
     * @throws InvalidArgumentException            If the given ScalingAlgorithm has an empty string as its name
     * @throws NotEnoughArgumentsException         If the given pixel format is null or an empty string
     * @throws UnsupportedOperatingSystemException If the user's Operating System is not yet supported by this library
     */
    public void setScaleParams(boolean development, @NotNull Scale scale, @Nullable ScalingAlgorithm alg, @NotNull String width,
                               @NotNull String height, @NotNull String incolmatname, @NotNull String outcolmatname,
                               @NotNull String incolrange, @NotNull String outcolrange, @NotNull String evalSize,
                               @NotNull String interlMode, @NotNull String forceOAsRatio, int divisibleBy)
            throws InvalidArgumentException, UnsupportedOperatingSystemException, NotEnoughArgumentsException {
        if (alg != null) {
            scale.setSwsFlags(alg);
        }
        scale.setSize(development, width, height, pixelFormat);
        scale.setInputColorMatrix(incolmatname);
        scale.setOutColorMatrix(outcolmatname);
        scale.setInputRange(incolrange);
        scale.setOutputRange(outcolrange);
        scale.setEval(evalSize);
        scale.setInterl(interlMode);
        scale.forceOriginalAspectRatio(forceOAsRatio);
        scale.setDivisibleBy(divisibleBy);
        scale.updateMap();
    }

    /**
     * Sets the "scale" filter parameters.
     *
     * @param scale         The "scale" filter instance
     * @param alg           The ScalingAlgorithm instance
     * @param videoSizeID   A parameter that sets the video size id according to the FFmpeg documentation on this matter
     * @param incolmatname  The name of the input color matrix, as described by FFmpeg's documentation of the scaling filter
     * @param outcolmatname The name of the output color matrix, as described by FFmpeg's documentation of the scalingù
     *                      filter
     * @param incolrange    The input color range
     * @param outcolrange   The output color range
     * @param evalSize      The value that tells when to evaluate the expressions for width and height
     * @param interlMode    The interlacing mode
     * @param forceOAsRatio A parameter that tells the program whether to force the original aspect ratio
     * @param divisibleBy   An integer that tells the program what the width and height should be divisible by
     * @throws InvalidArgumentException If the given ScalingAlgorithm has an empty string as its name
     */
    public void setScaleParamsWithSizeID(@NotNull Scale scale, @Nullable ScalingAlgorithm alg, @NotNull String videoSizeID,
                                         @NotNull String incolmatname, @NotNull String outcolmatname, @NotNull String incolrange,
                                         @NotNull String outcolrange, @NotNull String evalSize, @NotNull String interlMode,
                                         @NotNull String forceOAsRatio, int divisibleBy) throws InvalidArgumentException {
        if (alg != null) {
            scale.setSwsFlags(alg);
        }
        scale.setVideoSizeID(videoSizeID);
        scale.setInputColorMatrix(incolmatname);
        scale.setOutColorMatrix(outcolmatname);
        scale.setInputRange(incolrange);
        scale.setOutputRange(outcolrange);
        scale.setEval(evalSize);
        scale.setInterl(interlMode);
        scale.forceOriginalAspectRatio(forceOAsRatio);
        scale.setDivisibleBy(divisibleBy);
        scale.updateMap();
    }

    /**
     * Sets the parameters of a Format filter instance.
     *
     * @param format The given Format filter instance
     * @return The given Format filter instance with all parameters set
     * @throws InvalidArgumentException If the set pixel format is null or an empty string
     */
    @Contract("_ -> param1")
    public @NotNull Format setFormat(@NotNull Format format) throws InvalidArgumentException {
        format.addPixelFormat(pixelFormat);
        /*if(isOutFullRange) {
            format.addColorRange("pc"); //Full range
        } else {
            format.addColorRange("tv"); //Limited range
        }*/
        format.updateMap();

        return format;
    }

    /**
     * Sets the video simple filter graph.
     *
     * @param vfg The given video simple filter graph
     * @throws InvalidArgumentException If the given video simple filter graph is null or not an instance of VideoSimpleFilterGraph
     *                                  or the complex filter graph is not null
     */
    public void setVideoSimpleFilterGraph(@NotNull FilterGraph vfg) throws InvalidArgumentException {
        if (vfg == null || !(vfg instanceof VideoFilterGraph)) {
            throw new InvalidArgumentException("The video filter graph must be an instance of VideoSimpleFilterGraph.",
                    "Il grafo del filtro video deve essere un'istanza di VideoSimpleFilterGraph.");
        }

        if (cfg != null) {
            throw new InvalidArgumentException("The complex filter graph must be null in order to set the video simple " +
                    "filter graph.", "Il grafo complesso dei filtri deve essere null per impostare quello semplice dei " +
                    "filtri video.");
        }

        this.vfg = vfg;
    }

    /**
     * Sets the audio simple filter graph
     *
     * @param afg The given audio simple filter graph
     * @throws InvalidArgumentException If the given audio simple filter graph is null or not an instance of AudioSimpleFilterGraph
     *                                  or the complex filter graph is not null
     */
    public void setAudioSimpleFilterGraph(@NotNull FilterGraph afg) throws InvalidArgumentException {
        if (afg == null || !(afg instanceof AudioFilterGraph)) {
            throw new InvalidArgumentException("The audio filter graph must be an instance of AudioSimpleFilterGraph.",
                    "Il grafo del filtro audio deve essere un'istanza di AudioSimpleFilterGraph.");
        }

        if (cfg != null) {
            throw new InvalidArgumentException("The complex filter graph must be null in order to set the audio simple " +
                    "filter graph.", "Il grafo complesso dei filtri deve essere null per poter impostare il grafo semplice " +
                    "dei filtri audio.");
        }

        this.afg = afg;
    }

    /**
     * Sets the complex filter graph.
     *
     * @param cfg The given complex filter graph
     * @throws InvalidArgumentException If the given complex filter graph is null or the video or audio filter graphs are
     *                                  not null
     */
    public void setComplexFilterGraph(@NotNull FilterGraph cfg) throws InvalidArgumentException {
        if (cfg == null) {
            throw new InvalidArgumentException("The given complex filter graph cannot be null.", "Il grafo dei filtri " +
                    "non puo' essere null.");
        }
        if (vfg != null || afg != null) {
            throw new InvalidArgumentException("The video and audio filter graphs must be null in order to set the complex " +
                    "filter graph.", "I grafi dei filtri video e audio devono essere null per poter impostare quello complesso.");
        }
        this.cfg = cfg;
    }

    /**
     * Sets the "-fps_mode" option parameters.
     *
     * @param parameter        The "parameter" value. Cannot be null or empty
     * @param stream_specifier The "stream_specifier" value. Can be null or empty.
     * @throws InvalidArgumentException If the given "parameter" value is null or an empty string
     */
    public void setFPSMode(@NotNull String parameter, String stream_specifier) throws InvalidArgumentException {
        fps_mode = new FPSMode();
        if (!StringExt.checkNullOrEmpty(stream_specifier)) {
            fps_mode.setStreamSpecifier(stream_specifier);
        }
        fps_mode.setParameter(parameter);
    }

    /**
     * This method creates the command that, when run, will create the output video. WARNING: all needed filters, filter
     * chains and filter graphs must be set BEFORE calling this method.
     */
    public void createCommand() {
        try {
            //Sistemare sincronizzazione audio e video riscrivendo queste due righe in modo tale da supportare la
            //comunicazione del numero di secondi per entrambe le opzioni.
            builder.add("-async 1");

            if (fps_mode != null) {
                builder.add(fps_mode.toString());
            }
            builder.add("-r " + frameRate);
            builder.addAllInputs(pattern);

            if (pixelFormat == null || pixelFormat.isEmpty()) {
                //ATTENZIONE: il codec mjpeg non supporta il formato yuv420p perché non è un formato full-range!
                pixelFormat = "yuv420p";
            }

            if (codecID != null && !codecID.isEmpty()) {
                if (videoStreamCopy) {
                    //No video filtering allowed when stream copying video
                    builder.add("-c:v copy");
                } else {
                    builder.add("-c:v " + codecID);

                    if (vfg != null) {
                        builder.add(vfg.toString());
                    }
                }
            }
            if (audioCodec != null && !audioCodec.isEmpty()) {
                if (audioStreamCopy) {
                    //No audio filtering allowed when stream copying audio
                    builder.add("-c:a copy");
                } else {
                    builder.add("-c:a " + audioCodec);

                    if (afg != null) {
                        builder.add(afg.toString());
                    }
                }
            }
            if (cfg != null) {
                builder.add(cfg.toString());
            }
            if (videoBitRate != null && !videoBitRate.isEmpty()) {
                builder.add("-b:v " + videoBitRate);
            }
            if (audioBitRate != null && !audioBitRate.isEmpty()) {
                builder.add("-b:a " + audioBitRate);
            }
            if (startInstant > 0) {
                builder.add("-ss " + startInstant);
            }
            if (videoDuration > 0) {
                builder.add("-t " + videoDuration);
            }
            if (videoQuality != 0) {
                builder.add("-q:v " + videoQuality);
            }
            builder.addOutput(outputFile);
        } catch (InvalidArgumentException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
}
