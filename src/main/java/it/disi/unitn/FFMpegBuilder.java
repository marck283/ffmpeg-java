package it.disi.unitn;

import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Builder class for convenience class FFMpeg
 */
public class FFMpegBuilder {
    private String command;

    /**
     * This constructor initializes the class with the path to ffmpeg's command line utility.
     * @param ffmpegPath The relative path to ffmpeg's command line utility. This parameter must not be null only if
     *                   the user is operating on a Windows Operating System.
     * @throws NotEnoughArgumentsException when this constructor's argument is set to null
     */
    public FFMpegBuilder(@Nullable String ffmpegPath) throws NotEnoughArgumentsException {
        if(SystemUtils.IS_OS_WINDOWS) {
            if(ffmpegPath == null) {
                throw new NotEnoughArgumentsException("The arguments to FFMpegBuilder's constructor cannot be null.");
            }
            command = ffmpegPath;
        } else {
            command = "ffmpeg";
        }
    }

    /**
     * Creates a new FFMpeg instance using the information given to this FFMpegBuilder instance.
     * @return The created FFMpeg instance
     */
    public FFMpeg build() {
        return new FFMpeg(this);
    }

    /**
     * Convenience method to get the ffmpeg command to be executed.
     * @return The command to be executed
     */
    public String getCommand() {
        return command;
    }

    /**
     * Sets this Builder's command to the given ffmpeg command.
     * @param newCmd The given ffmpeg command
     */
    public void setCommand(@NotNull String newCmd) {
        command = newCmd;
    }

    /**
     * This method is internally called in order to reset this Builder's command when the user is operating on a Windows
     * Operating System. It is required that the parameter to this method be not null.
     * @param pathToFFMpeg The path to the ffmpeg executable
     */
    private void resetCommandWindows(@NotNull String pathToFFMpeg) {
        command = "\"" + pathToFFMpeg + "\"";
    }

    /**
     * This method allows the user to reset the command of this Builder.
     * @param pathToFFMpeg The path to the ffmpeg executable. This parameter must not be null only if the user is operating
     *                     on a Windows Operating System.
     * @throws NotEnoughArgumentsException If the user is operating on a Windows Operating System and the parameter of
     * this method is null.
     * @throws UnsupportedOperatingSystemException If the user is operating on an operating system that is not supported
     * by this library (i.e., all operating systems other than Windows and Linux OSs).
     */
    public void resetCommand(@Nullable String pathToFFMpeg) throws NotEnoughArgumentsException,
            UnsupportedOperatingSystemException {
        if(SystemUtils.IS_OS_WINDOWS) {
            if(pathToFFMpeg != null) {
                resetCommandWindows(pathToFFMpeg);
            } else {
                throw new NotEnoughArgumentsException("The argument to this method cannot be null.");
            }
        } else {
            if(SystemUtils.IS_OS_LINUX) {
                command = "ffmpeg";
            } else {
                throw new UnsupportedOperatingSystemException();
            }
        }
    }

    /**
     * Adds the path to an input file to the command to be executed.
     * @param inputFile The input file whose path is to be added to the command
     */
    public void addInput(@NotNull String inputFile) {
        command += " -i \"" + inputFile + "\"";
    }

    /**
     * Create a new TracksMerger instance given a video output file and an audio and a video input file.
     * @param outputFile The given video output file
     * @param audioInput The given audio input file
     * @param videoInput The given video input file
     * @return The newly created TracksMerger instance
     * @throws NotEnoughArgumentsException when the TracksMerger's constructor throws this exception
     */
    public TracksMerger newTracksMerger(@NotNull String outputFile, @NotNull String audioInput,
                                        @NotNull String videoInput) throws NotEnoughArgumentsException {
        return new TracksMerger(this, outputFile, audioInput, videoInput);
    }

    /**
     * This method performs the same action as newTracksMerger(String, String, String), but it should be used only when
     * creating the final output video.
     * @param outputFile The path to the output file
     * @return The newly created TracksMerger instance
     * @throws NotEnoughArgumentsException when the TracksMerger's constructor throws this exception
     */
    public TracksMerger newTracksMerger(@NotNull String outputFile) throws NotEnoughArgumentsException {
        return new TracksMerger(this, outputFile);
    }

    /**
     * Returns a new VideoCreator instance given the path to the output file and a non-null list of paths to the input images.
     * @param outputFile The path to the output file
     * @param inputFolder The non-null path to the folder containing the input images
     * @param pattern The pattern that specifies the names of the files to be included in the output video.
     *                This path has to be ffmpeg-compatible, and it must include the file extensions.
     * @return A new VideoCreator instance
     * @throws NotEnoughArgumentsException when at least one of the given arguments is null
     */
    public VideoCreator newVideoCreator(@NotNull String outputFile, @NotNull String inputFolder, @NotNull String pattern)
            throws NotEnoughArgumentsException {
        return new VideoCreator(this, outputFile, inputFolder, pattern);
    }

    /**
     * Same as for addInput(), but this can add multiple files. Throws NotEnoughArgumentsException if the input value is
     * null or if one of the input values is null.
     * @param inputFiles The list of paths of the input files to be added to the command
     * @throws NotEnoughArgumentsException when one of the arguments given to this method is null
     */
    public void addAllInputs(@NotNull String @NotNull ... inputFiles) throws NotEnoughArgumentsException {
        if(inputFiles == null) {
            throw new NotEnoughArgumentsException("None of the arguments given to this method can be null.");
        }
        for(String s: inputFiles) {
            if(s == null) {
                throw new NotEnoughArgumentsException("None of the arguments given to this method can be null.");
            }
        }
        StringBuilder newCmd = new StringBuilder(command);
        for(String s: inputFiles) {
            newCmd.append(" -i \"").append(s).append("\"");
        }
        command = newCmd.toString();
    }

    /**
     * Adds the path of an output file to the command.
     * @param outputFile The path of the output file to be added
     */
    public void addOutput(@NotNull String outputFile) {
        command += " -y \"" + outputFile + "\"";
    }

    /**
     * Sets the video frame size from the given sizeID.
     * See <a href="https://ffmpeg.org/ffmpeg-all.html#Video-size">here</a> for the recognized size IDs.
     * @param sizeID The given size ID
     */
    public void setVideoFrameSize(@NotNull String sizeID) {
        command += " -video_size " + sizeID;
    }

    /**
     * Sets the video frame size on the given width and height parameters.
     * @param width The width of the frame
     * @param height The height of the frame
     */
    public void setVideoFrameSize(int width, int height) {
        command += " -video_size " + width + "x" + height;
    }
}
