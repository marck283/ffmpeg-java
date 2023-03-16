package it.disi.unitn;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import org.jetbrains.annotations.NotNull;

public class VideoCreator {

    private FFMpegBuilder builder;
    private int frameRate = 1; //Frame rate

    private String outputFile = "", codecID = ""; //Name of output file and codec ID

    private String[] inputImages; //Paths to input images

    private int videoQuality = 0; //Quality of output video. For "x264", sane values should be between 18 and 28

    private int videoWidth = 0, videoHeight = 0;

    private String videoSizeID = "";

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

    public void setFrameRate(int val) throws InvalidArgumentException {
        if(val <= 0) {
            throw new InvalidArgumentException("The frame rate value should always be greater than or equal to 1.");
        } else {
            frameRate = val;
        }
    }

    public void setCodecID(@NotNull String codecID) {
        this.codecID = codecID; //TODO: check if this value is valid using the accepted codecs from ffmpeg
    }

    public void setVideoQuality(int quality) {
        videoQuality = quality;
    }

    public void setVideoSize(int width, int height) {
        videoWidth = width;
        videoHeight = height;
    }

    public void setVideoSizeID(@NotNull String videoSizeID) throws InvalidArgumentException {
        //TODO: check if the id of the size is recognizable by ffmpeg
        if(videoSizeID == null) {
            throw new InvalidArgumentException("The video size ID should not be null and it should be recognized by ffmpeg.");
        }
        this.videoSizeID = videoSizeID;
    }

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
            if(videoQuality != 0) {
                builder.setCommand(builder.getCommand() + " -crf " + videoQuality);
            }
            builder.setCommand(builder.getCommand() + " " + outputFile);
        }
    }
}
