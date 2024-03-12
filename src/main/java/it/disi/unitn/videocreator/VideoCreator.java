package it.disi.unitn.videocreator;

import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.NotEnoughArgumentsException;
//import org.apache.commons.exec.*;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import org.apache.commons.exec.CommandLine;
//import org.apache.commons.exec.Watchdog;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
/*import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;*/
import java.nio.file.*;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to create videos using different options compatible with ffmpeg.
 */
public class VideoCreator {

    private final FFMpegBuilder builder;
    private int frameRate = 1, videoDuration; //Frame rate and video duration

    private final String outputFile; //Name of output file

    private String codecID = "h264", audioCodec; //Name of video and audio codec ID

    private final String folder; //Folder to search into when executing the command

    //Pattern of the names of the files to be included in the video or name of the file to be included in the video
    private final String pattern;

    private int videoQuality = 0; //Quality of output video. For "x264", sane values should be between 18 and 28

    private int videoWidth = 0, videoHeight = 0;

    private String videoSizeID = "";

    private int startInstant; //Starting instant of the new video with respect to the original video

    private String audioBitRate; //Audio track's bitrate (in Kbit/s)

    private String videoBitRate; //Video track's bitrate (in Kbit/s or Mbit/s)

    private String pixelFormat;

    private String execFile = "";

    private boolean isOutFullRange;

    /**
     * The constructor of this class.
     * @param builder The FFMpegBuilder instance that called this constructor
     * @param outputFile The path to the output file
     * @param inputFolder The path to the folder containing the input files
     * @param pattern The pattern of the names of the images to include in the video
     *                or the name of the file to be included in the video
     * @throws NotEnoughArgumentsException if any of the arguments given to this constructor is null
     */
    public VideoCreator(@NotNull FFMpegBuilder builder, @NotNull String outputFile,
                        @NotNull String inputFolder, @NotNull String pattern) throws NotEnoughArgumentsException {
        if(builder == null || inputFolder == null || outputFile == null || pattern == null) {
            throw new NotEnoughArgumentsException("The arguments given to the VideoCreator constructor cannot be null.");
        } else {
            folder = inputFolder;
            this.pattern = pattern;
            this.builder = builder;
            this.outputFile = outputFile;
            if(SystemUtils.IS_OS_LINUX) {
                execFile = "./src/ffcodec/bin/linux/ffcodec";
            } else {
                if(SystemUtils.IS_OS_WINDOWS) {
                    execFile = "./src/ffcodec/bin/windows/ffcodec.exe";
                }
            }
            isOutFullRange = false;
        }
    }

    /**
     * This method sets the output video's frame rate.
     * @param val The output video's frame rate
     * @throws InvalidArgumentException when the given frame rate is less than or equal to 0.
     */
    public void setFrameRate(int val) throws InvalidArgumentException {
        if(val <= 0) {
            throw new InvalidArgumentException("The frame rate value should always be greater than or equal to 1.");
        } else {
            frameRate = val;
        }
    }

    /**
     * This method sets the duration of the output video.
     * @param val The duration of the output video
     * @throws InvalidArgumentException when the given duration is less than or equal to 0.
     */
    public void setVideoDuration(int val) throws InvalidArgumentException {
        if(val <= 0) {
            throw new InvalidArgumentException("The video duration must always be of at least 1 second.");
        }
        videoDuration = val;
    }

    /**
     * This method sets the starting instant of the resulting video, but it requires that the input file be itself a video
     * of any format supported by ffmpeg.
     * @param val The starting instant of the new video
     * @throws InvalidArgumentException when the starting instant of the new video is less than or equal to 0.
     */
    public void setStartInstant(int val) throws InvalidArgumentException {
        if(val <= 0) {
            throw new InvalidArgumentException("The starting instant of the new video must be at least the starting instant" +
                    " of the original video.");
        }
        startInstant = val;
    }

    /**
     * This method checks if the given file path refers to an executable file.
     * @return True if thr path refers to an executable file, otherwise false.
     */
    private boolean checkExecutable() {
        if(!Files.isExecutable(Paths.get(execFile))) {
            Locale l = Locale.getDefault();
            if(l == Locale.ITALY || l == Locale.ITALIAN) {
                System.err.println("Non e' possibile eseguire il file " + execFile + ". Si prega di controllarne i permessi " +
                        "di esecuzione e l'esistenza.");
            } else {
                System.err.println("Cannot execute file " + execFile + ". Please check the user's permissions and that the file exists.");
            }

            return false;
        }

        return true;
    }

    /**
     * This method executes the given Command Line command.
     * @param executor A DefaultExecutor instance
     * @param execResHandler An ExecutorResHandler instance
     * @param cmdline A CommandLine instance
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
            if(val == 0) {
                Locale l = Locale.getDefault();
                if(l == Locale.ITALY || l == Locale.ITALIAN) {
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
     * @param cmdline The CommandLine instance
     * @param s The given codec
     * @return True if the user's installation of FFmpeg supports the given codec, otherwise false
     * @throws IOException If an I/O error occurs
     * @throws InvalidArgumentException if the given codec is null or an empty string
     */
    private boolean performCheck(@NotNull CommandLine cmdline, String s) throws IOException, InvalidArgumentException {
        try {
            if(!checkExecutable()) {
                return false;
            }

            Path tempFile = Files.createTempFile("ffmpeg-java-temp", ".txt");
            BufferedOutputStream outstream = new BufferedOutputStream(Files.newOutputStream(tempFile,
                    StandardOpenOption.WRITE));
            PumpStreamHandler streamHandler = new PumpStreamHandler(outstream, System.err);
            DefaultExecutor executor = new DefaultExecutor();
            executor.setStreamHandler(streamHandler);
            ExecutorResHandler execResHandler = new ExecutorResHandler(outstream, tempFile/*, s*/);
            return executeCML(executor, execResHandler, cmdline);
        } catch(InvalidPathException ex) {
            System.err.println(ex.getLocalizedMessage());
        }

        return false;
    }

    /**
     * This method checks if the given codec is supported by the current installation of FFmpeg.
     * @param s The given codec ID.
     * @return true if the given codec is supported, otherwise false
     * @throws IOException If an I/O error occurs
     * @throws InvalidArgumentException if the given codec is null or an empty string
     * @throws UnsupportedOperatingSystemException if the Operating System is not yet supported.
     */
    private boolean enumCodecs(String s) throws IOException, InvalidArgumentException, UnsupportedOperatingSystemException {
        CommandLine cmdline;
        if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_WINDOWS) {
            cmdline = CommandLine.parse(execFile + " " + s);
            return performCheck(cmdline, s);
        } else {
            throw new UnsupportedOperatingSystemException();
        }
    }

    /**
     * This method checks if the given encoding codec ID is supported by ffmpeg.
     * @param codecID The given codec ID
     * @return true if the given codec is valid, otherwise false
     * @throws IOException if an I/O error occurs
     * @throws InvalidArgumentException if the codec ID is null or an empty string or an internal error occurred
     */
    private boolean checkCodec(@NotNull String codecID) throws IOException,
            InvalidArgumentException, UnsupportedOperatingSystemException {
        if(codecID == null || codecID.isEmpty()) {
            throw new InvalidArgumentException("The argument to this method must not be null or an empty string.");
        }
        /*switch (codecID) {
            case "a64_multi", "a64_multi5", "alias_pix", "amv", "apng", "asv1", "asv2", "libaom-av1", "librav1e",
                    "libsvtav1", "av1_nvenc", "av1_qsv", "av1_amf", "avrp", "avui", "ayuv", "bitpacked", "bmp", "cfhd",
                    "cinepak", "cljr", "dnxhd", "dpx", "dvvideo", "exr", "ffv1", "ffvhuff", "fits", "flashsv", "flashsv2",
                    "flv", "gif", "h261", "h263", "h263p", "h264", "hap", "hdr", "hevc", "huffyuv", "jpeg200", "libopenjpg",
                    "jpegls", "ljpeg", "magicyuv", "mjpeg", "mpeg1video", "mpeg2video", "mpeg2_qsv", "mpeg4", "msmpeg4v2",
                    "msmpeg4", "msvideo1", "pam", "pbm", "pcx", "pfm", "pgm", "pgmyuv", "phm", "png", "ppm", "prores",
                    "qoi", "qtrle", "r10k", "r210", "rawvideo", "roq", "rpza", "rv10", "rv20", "sgi", "smc", "snow",
                    "speedhq", "sunrast", "svq1", "targa", "libtheora", "tiff", "utvideo", "v210", "v308", "v408", "v410",
                    "vbn", "vnull", "libvpx", "libvpx-vp9", "vp9_qsv", "wbmp", "libwebp-anim", "libwebp", "wmv1", "wmv2",
                    "wrapped_avframe", "xbm", "xface", "xwd", "y41p", "yuv4", "zlib", "zmbv" -> {
                return true;
            }
            default -> {
                return false;
            }
        }*/

        return enumCodecs(codecID);

        //Thread per stampare su file i codec supportati da FFmpeg ed eseguire grep per verificare che il codec
        //richiesto sia supportato.
        /*Path tempFile = Files.createTempFile("ffmpeg-java-temp", ".txt");
        BufferedOutputStream outstream = new BufferedOutputStream(Files.newOutputStream(tempFile,
                StandardOpenOption.WRITE));
        ExecutorResHandler execResHandler = new ExecutorResHandler(outstream, tempFile, codecID);
        CommandLine cmdline = CommandLine.parse(builder.getCommand() + " -codecs -hide_banner");
        PumpStreamHandler streamHandler = new PumpStreamHandler(outstream, System.err);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(streamHandler);
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

            return execResHandler.getValue() == 1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    /**
     * This method sets the codec ID of the output video.
     * @param codecID The given codec ID
     * @throws NotEnoughArgumentsException if the given codec ID is null
     * @throws InvalidArgumentException if the given codec ID is not supported by ffmpeg
     * @throws IOException if an I/O error occurs
     * @throws UnsupportedOperatingSystemException if the Operating System is not yet supported
     */
    public void setCodecID(@NotNull String codecID) throws NotEnoughArgumentsException, InvalidArgumentException,
            IOException, UnsupportedOperatingSystemException {
        if(codecID == null) {
            throw new NotEnoughArgumentsException("The codec id must not be null.");
        }
        if(checkCodec(codecID)) {
            this.codecID = codecID;
            if(codecID.equals("h264")) {
                setPixelFormat("yuv420p");
            }
            if(codecID.equals("mjpeg")) {
                setPixelFormat("yuvj420p");
            }
        } else {
            Locale l = Locale.getDefault();
            if(l == Locale.ITALY || l == Locale.ITALIAN) {
                throw new InvalidArgumentException("Codec video non valido.");
            } else {
                throw new InvalidArgumentException("Invalid video codec.");
            }
        }
    }

    /**
     * This method allows the user to set the audio codec for the resulting video. WARNING: the audio codec should always
     * be compatible with the video codec.
     * @param ac The audio codec to be used in the resulting video
     */
    public void setAudioCodec(@NotNull String ac) {
        if(ac == null || ac.isEmpty()) {
            throw new IllegalArgumentException();
        }
        audioCodec = ac;
    }

    /**
     * This method checks if the given dimensions are accepted by FFmpeg.
     * @param width The width
     * @param height The height
     * @return True if the given dimensions are accepted, false otherwise
     */
    private boolean checkSize(int width, int height) {
        CommandLine cmdLine = CommandLine.parse("./src/ffcodec/bin/linux/checkSize/main " + width + " " + height);
        PumpStreamHandler streamHandler = new PumpStreamHandler();
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(streamHandler);
        ExecutorResHandler execResHandler = new ExecutorResHandler(/*width, height*/);
        return executeCML(executor, execResHandler, cmdLine);
    }

    /**
     * This method checks if the given size ID is accepted by FFmpeg.
     * @param sizeID The given size ID
     * @return True if the given size ID is accepted by FFmpeg, otherwise false
     */
    private boolean checkSizeID(@NotNull String sizeID) {
        return switch (sizeID) {
            case "ntsc", "pal", "qntsc", "qpal", "sntsc", "spal", "film", "ntsc-film", "sqcif", "qcif", "cif", "4cif",
                    "16cif", "qqvga", "qvga", "vga", "svga", "xga", "uxga", "qxga", "sxga", "qsxga", "hsxga", "wvga",
                    "wxga", "wsxga", "wuxga", "woxga", "wqsxga", "wquxga", "whsxga", "whuxga", "cga", "ega", "hd480",
                    "hd720", "hd1080", "2k", "2kflat", "2kscope", "4k", "4kflat", "4kscope", "nhd", "hqvga", "wqvga",
                    "fwqvga", "hvga", "qhd", "2kdci", "4kdci", "uhd2160", "uhd4320" ->
                    true;
            default -> false;
        };
    }

    /**
     * This method sets the width and height of the resulting video by means of the given video size ID.
     * @param videoSizeID The given video size ID
     * @throws InvalidArgumentException if the given video size ID is not supported by ffmpeg
     */
    public void setVideoSizeID(@NotNull String videoSizeID) throws InvalidArgumentException {
        if(videoSizeID == null) {
            throw new InvalidArgumentException("The video size ID should not be null and it should be recognized by ffmpeg.");
        }
        if(checkSizeID(videoSizeID)) {
            this.videoSizeID = videoSizeID;
        } else {
            Locale l = Locale.getDefault();
            if(l == Locale.ITALIAN || l == Locale.ITALY) {
                System.err.println("Risoluzione immagine non valida.");
            } else {
                System.err.println("Invalid image resolution.");
            }
        }
    }

    /**
     * This method sets the quality of the output video.
     * @param quality The quality of the output video
     */
    public void setVideoQuality(int quality) {
        videoQuality = quality;
    }

    /**
     * This method sets the size of the resulting video by means of the given width and height.
     * @param width The given width
     * @param height The given height
     * @throws InvalidArgumentException if the given width or height parameter is less than or equal to 0
     */
    public void setVideoSize(int width, int height) throws InvalidArgumentException {
        if(width <= 0 || height <= 0) {
            throw new InvalidArgumentException("The given width or height parameter must be a strictly positive integer value.");
        }
        if(checkSize(width, height)) {
            videoWidth = width;
            videoHeight = height;
        }
    }

    /**
     * This method sets the audio track's bitrate (in Kbit/s).
     * @param val The audio track's bitrate
     * @throws InvalidArgumentException of the given bitrate value is less than or equal to 0
     */
    private void setAudioBitRate(int val) throws InvalidArgumentException {
        if(val <= 0) {
            throw new InvalidArgumentException("The bitrate of the audio track must be greater than 0.");
        }
        audioBitRate = val + "k";
    }

    /**
     * This method sets the output video's bitrate in Kilobits or Megabits.
     * @param val The bitrate value
     * @param mode A value between "k" and "m" (short for Kilobit and Megabit respectively)
     * @throws InvalidArgumentException if the "mode" argument is null, or it is not equal to "k" or "m"
     */
    public void setVideoBitRate(int val, @NotNull String mode) throws InvalidArgumentException {
        if(mode == null || (!mode.equals("k") && !mode.equals("m"))) {
            throw new InvalidArgumentException("The \"mode\" parameter must be specified and it must be either \"k\" or \"m\".");
        }
        videoBitRate = val + mode;
    }

    /**
     * Imposta il formato dei pixel.
     * @param pxfmt L'identificatore del formato dei pixel.
     * @throws InvalidArgumentException Se l'argomento fornito in input è null.
     */
    public void setPixelFormat(@NotNull String pxfmt) throws InvalidArgumentException {
        if(pxfmt == null || pxfmt.isEmpty()) {
            throw new InvalidArgumentException("The argument to this method cannot be null.");
        }
        pixelFormat = pxfmt;
    }

    /**
     * Questo metodo imposta la modalità di resa dei colori.
     * @param val Valore booleano per indicare la modalità di resa dei colori
     */
    public void setOutFullRange(boolean val) {
        isOutFullRange = val;
    }

    /**
     * This method creates the command that, when run, will create the output video.
     * @param time The maximum amount of time to wait for the video's creation
     * @param timeUnit The TimeUnit instance to be used
     * @throws InvalidArgumentException if the video width or height or the video size ID field is null
     * @throws IOException If an I/O error occurs
     */
    public void createCommand(long time, @NotNull TimeUnit timeUnit) throws InvalidArgumentException, IOException {
        if((videoWidth == 0 || videoHeight == 0) && (videoSizeID == null || videoSizeID.isEmpty())) {
            throw new InvalidArgumentException("The video width and height should not be null or empty strings.");
        } else {
            if(timeUnit == null) {
                throw new InvalidArgumentException("The given time unit should not be null.");
            } else {
                if(time <= 0) {
                    throw new InvalidArgumentException("The given time should not be less than or equal to zero.");
                } else {
                    builder.setCommand(builder.getCommand() + " -r " + frameRate);
                    /*if(videoWidth != 0 && videoHeight != 0) {
                        builder.setCommand(builder.getCommand() + " -vf \"scale=" + videoWidth + ":" + videoHeight + "\"");
                    } else {
                        builder.setCommand(builder.getCommand() + " -s " + videoSizeID);
                    }*/

                    builder.addInput(folder + "/" + pattern);

                    if(pixelFormat == null || pixelFormat.isEmpty()) {
                        //ATTENZIONE: il codec mjpeg non supporta il formato yuv420p perché non è un formato full-range!
                        pixelFormat = "yuv420p";
                    }

                    String scale = "\"scale=";
                    builder.setCommand(builder.getCommand() + " -pix_fmt " + pixelFormat);
                    if(codecID != null && !codecID.isEmpty()) {
                        builder.setCommand(builder.getCommand() + " -c:v " + codecID);
                        if(codecID.equals("h264")) {
                            //h264 (default codec when no value is specified) needs even width and height, so we need to add
                            //this filter in order to divide them by 2.
                            scale = scale.concat("ceil(.5*iw)*2:ceil(.5*ih)*2");
                            if(isOutFullRange) {
                                scale = scale.concat(":out_range=full");
                            }
                            builder.setCommand(builder.getCommand() + " -vf " + scale + "\"");
                        } else {
                            if(videoWidth != 0 && videoHeight != 0) {
                                scale = scale.concat( + videoWidth + ":" + videoHeight);
                                if(isOutFullRange) {
                                    scale = scale.concat(":out_range=full");
                                }
                                scale = scale.concat(",format=" + pixelFormat);
                                builder.setCommand(builder.getCommand() +  " -vf " + scale + "\"");
                            } else {
                                builder.setCommand(builder.getCommand() + " -video_size " + videoSizeID);
                            }
                        }
                    }
                    if(audioCodec != null && !audioCodec.isEmpty()) {
                        builder.setCommand(builder.getCommand() + " -c:a " + audioCodec);
                    }
                    if(videoBitRate != null && !videoBitRate.isEmpty()) {
                        builder.setCommand(builder.getCommand() + " -b:v " + videoBitRate);
                    }
                    if(audioBitRate != null && !audioBitRate.isEmpty()) {
                        builder.setCommand(builder.getCommand() + " -b:a " + audioBitRate);
                    }
                    if(startInstant > 0) {
                        builder.setCommand(builder.getCommand() + " -ss " + startInstant);
                    }
                    if(videoDuration > 0) {
                        builder.setCommand(builder.getCommand() + " -t " + videoDuration);
                    }
                    if(videoQuality != 0) {
                        builder.setCommand(builder.getCommand() + " -q:v " + videoQuality);
                    }
                    builder.addOutput(outputFile);

                    //Now we will execute the given command
                    FFMpeg ffmpeg = builder.build();
                    ffmpeg.executeCMD(time, timeUnit);
                }
            }
        }
    }
}
