package it.disi.unitn;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import org.jetbrains.annotations.NotNull;

public class VideoCreator {

    private final FFMpegBuilder builder;
    private int frameRate = 1, videoDuration; //Frame rate and video duration

    private final String outputFile; //Name of output file
    private String codecID = ""; //Name of codec ID

    private final String[] inputImages; //Paths to input images

    private int videoQuality = 0; //Quality of output video. For "x264", sane values should be between 18 and 28

    private int videoWidth = 0, videoHeight = 0;

    private String videoSizeID = "";

    private int startInstant; //Starting instant of the new video with respect to the original video

    public VideoCreator(@NotNull FFMpegBuilder builder, @NotNull String outputFile,
                        @NotNull String @NotNull ... inputImages) throws NotEnoughArgumentsException {
        if(builder == null || inputImages == null || outputFile == null) {
            throw new NotEnoughArgumentsException("The arguments given to the VideoCreator constructor cannot be null.");
        } else {
            for(String s: inputImages) {
                if(s == null) {
                    throw new NotEnoughArgumentsException("The arguments given to the VideoCreator constructor cannot be null.");
                }
            }

            this.builder = builder;
            this.inputImages = inputImages;
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
     * This method checks if the given codec ID is supported by ffmpeg.
     * @param codecID The given codec ID
     * @return true if the given codec is valid, otherwise false
     */
    private boolean checkCodec(@NotNull String codecID) {
        switch (codecID) {
            case "a64_multi", "a64_multi5", "Cinepak", "GIF", "Hap", "jpeg2000", "libravle", "libaom-avl", "libsvtavl",
                    "libjxl", "libkvazaar", "libopenh264", "libtheora", "libvpx", "libwebp", "libx264", "libx264rgb",
                    "libx265", "libxavs2", "libxvid", "MediaFoundation", "mpeg2", "png", "ProRes", "snow", "vbn", "vc2" -> {
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
            for(String s: inputImages) {
                builder.setCommand(builder.getCommand() + " - i " + s);
            }
            if(codecID != null && !codecID.equals("")) {
                builder.setCommand(builder.getCommand() + " -vcodec " + codecID);
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
            builder.setCommand(builder.getCommand() + " " + outputFile);
        }
    }
}
