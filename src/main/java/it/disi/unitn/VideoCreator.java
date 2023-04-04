package it.disi.unitn;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to create videos using different options compatible with ffmpeg.
 */
public class VideoCreator {

    private final FFMpegBuilder builder;
    private int frameRate = 1, videoDuration; //Frame rate and video duration

    private final String outputFile; //Name of output file

    private String codecID = "libx264"; //Name of codec ID

    private final String folder; //Folder to search into when executing the command

    //Pattern of the names of the files to be included in the video or name of the file to be included in the video
    private final String pattern;

    private int videoQuality = 0; //Quality of output video. For "x264", sane values should be between 18 and 28

    private int videoWidth = 0, videoHeight = 0;

    private String videoSizeID = "";

    private int startInstant; //Starting instant of the new video with respect to the original video

    private String audioBitRate; //Audio track0s bitrate (in Kbit/s)

    private String videoBitRate; //Video track bitrate (in Kbit/s or Mbit/s)

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
     * This method checks if the given encoding codec ID is supported by ffmpeg.
     * @param codecID The given codec ID
     * @return true if the given codec is valid, otherwise false
     */
    private boolean checkCodec(@NotNull String codecID) {
        switch (codecID) {
            case "a64_multi", "a64_multi5", "alias_pix", "amv", "apng", "asv1", "asv2", "libaom-av1", "librav1e",
                    "libsvtav1", "av1_nvenc", "av1_qsv", "av1_amf", "avrp", "avui", "ayuv", "bitpacked", "bmp", "cfhd",
                    "cinepak", "cljr", "dnxhd", "dpx", "dvvideo", "exr", "ffv1", "ffvhuff", "fits", "flashsv", "flashsv2",
                    "flv", "gif", "h261", "h263", "h263p", "h264", "hap", "hdr", "hevc", "huffyuv", "jpeg200", "libopenjpg",
                    "jpegls", "ljpeg", "magicyuv", "mjpeg", "mpeg1video", "mpeg2video", "mpeg2_qsv", "mpeg4", "msmpeg4v2",
                    "msmpeg4", "msvideo1", "pam", "pbm", "pcx", "pfm", "pgm", "pgmyuv", "phm", "png", "ppm", "prores",
                    "qoi", "qtrle", "r10k", "r210", "rawvideo", "roq", "rpza", "rv10", "rv20", "sgi", "smc", "snow",
                    "speedhq", "sunrast", "svq1", "targa", "libtheora", "tiff", "utvideo", "v210", "v308", "v408", "v410",
                    "vbn", "vnull", "libvpx", "libvpx-vp9", "vp9_qsv", "wbmp", "libwebp-anim", "libwebp", "wmv1", "wmv2",
                    "wrapped_avframe", "xbm", "xface", "xwd", "y41p", "yuv4", "zlib", "zmbv", "libx264" -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * This method sets the codec ID of the output video.
     * @param codecID The given codec ID
     * @throws NotEnoughArgumentsException if the given codec ID is null
     * @throws InvalidArgumentException if the given codec ID is not supported by ffmpeg
     */
    public void setCodecID(@NotNull String codecID) throws NotEnoughArgumentsException, InvalidArgumentException {
        if(codecID == null) {
            throw new NotEnoughArgumentsException("The codec id must not be null.");
        }
        if(checkCodec(codecID)) {
            this.codecID = codecID;
        } else {
            throw new InvalidArgumentException("Invalid video codec");
        }
    }

    /**
     * This method sets the width and height of the resulting video by means of the given video size ID.
     * @param videoSizeID The given video size ID
     * @throws InvalidArgumentException if the given video size ID is not supported by ffmpeg
     */
    public void setVideoSizeID(@NotNull String videoSizeID) throws InvalidArgumentException {
        //TODO: check if the id of the size is recognizable by ffmpeg
        if(videoSizeID == null) {
            throw new InvalidArgumentException("The video size ID should not be null and it should be recognized by ffmpeg.");
        }
        this.videoSizeID = videoSizeID;
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
        videoWidth = width;
        videoHeight = height;
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
     * @throws InvalidArgumentException if the "mode" argument is null or it is not equal to "k" or "m"
     */
    public void setVideoBitRate(int val, @NotNull String mode) throws InvalidArgumentException {
        if(mode == null || (!mode.equals("k") && !mode.equals("m"))) {
            throw new InvalidArgumentException("The \"mode\" parameter must be specified and it must be either \"k\" or \"m\".");
        }
        videoBitRate = val + mode;
    }

    /**
     * This method creates the command than, when run, will create the output video.
     * @throws InvalidArgumentException if the video width or height or the video size ID field is null
     */
    public void createCommand() throws InvalidArgumentException {
        if(videoWidth == 0 || videoHeight == 0 || videoSizeID == null) {
            throw new InvalidArgumentException("The video width and height should not be null.");
        } else {
            builder.setCommand(builder.getCommand() + " -r " + frameRate);
            if(videoWidth != 0 && videoHeight != 0) {
                builder.setCommand(builder.getCommand() + " -s " + videoWidth + "x" + videoHeight);
            } else {
                builder.setCommand(builder.getCommand() + " -s " + videoSizeID);
            }

            //The following line works both on Windows and Linux systems
            builder.addInput(folder + "/" + pattern);

            //The following line will work only on Windows systems
            //builder.setCommand(builder.getCommand() + " -i " + folder + "/" + pattern);
            if(codecID != null && !codecID.equals("")) {
                builder.setCommand(builder.getCommand() + " -c:v " + codecID);
                if(codecID.equals("libx264")) {
                    //libx264 (default codec when no value is specified) needs even width and height, so we need to add
                    //this filter in order to divide them by 2.
                    builder.setCommand(builder.getCommand() + " -vf \"scale=ceil(.5*iw)*2:ceil(.5*ih)*2\"");
                }
            }
            if(videoBitRate != null && !videoBitRate.equals("")) {
                builder.setCommand(builder.getCommand() + " -b:v " + videoBitRate);
            }
            if(audioBitRate != null && !audioBitRate.equals("")) {
                builder.setCommand(builder.getCommand() + " -b:a " + audioBitRate);
            }
            if(startInstant > 0) {
                builder.setCommand(builder.getCommand() + " -ss " + startInstant);
            }
            if(videoDuration > 0) {
                builder.setCommand(builder.getCommand() + " -t " + videoDuration);
            }
            if(videoQuality != 0) {
                builder.setCommand(builder.getCommand() + " -crf " + videoQuality);
            }
            builder.setCommand(builder.getCommand() + " -pix_fmt yuv420p");
            builder.addOutput(outputFile);
        }
    }
}
