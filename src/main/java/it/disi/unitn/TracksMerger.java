package it.disi.unitn;

import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A class that helps merge audio and video tracks.
 */
public class TracksMerger {
    private final String videoOutput, audioInput, videoInput;

    private final FFMpegBuilder builder;

    private boolean streamCopy;

    TracksMerger(@NotNull FFMpegBuilder builder, @NotNull String outputFile, @NotNull String audioInput,
                        @NotNull String videoInput) throws NotEnoughArgumentsException {
        if(builder == null || audioInput == null || videoInput == null) {
            throw new NotEnoughArgumentsException("The arguments to TracksMerger's constructor cannot be null.");
        }

        this.builder = builder;
        this.audioInput = audioInput;
        this.videoInput = videoInput;
        videoOutput = outputFile;
    }

    TracksMerger(@NotNull FFMpegBuilder builder, @NotNull String outputVideo) throws NotEnoughArgumentsException {
        if(builder == null || outputVideo == null) {
            throw new NotEnoughArgumentsException("The arguments to TracksMerger's constructor cannot be null.");
        }

        this.builder = builder;
        videoOutput = outputVideo;
        audioInput = "";
        videoInput = "";
    }

    /**
     * This method enables (or disables) the stream-copy option.
     * @param val A boolean value used to denote whether the tracks should be stream-copied
     */
    public void streamCopy(boolean val) {
        streamCopy = val;
    }

    /**
     * This method sets the correct command to merge the audio and video tracks.
     * @param time The maximum amount of time to wait for the video's creation
     * @param timeUnit The TimeUnit instance to be used
     * @throws NotEnoughArgumentsException when the video input path or audio input path is null
     */
    public void mergeAudioWithVideo(long time, @NotNull TimeUnit timeUnit) throws NotEnoughArgumentsException {
        builder.addAllInputs(videoInput, audioInput);
        builder.setCommand(builder.getCommand() + " -map 0:v");
        if(streamCopy) {
            builder.setCommand(builder.getCommand() + " -c:v copy");
        }
        builder.setCommand(builder.getCommand() + " -c:a copy -map 1:a");
        builder.addOutput(videoOutput);

        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(time, timeUnit);
    }

    /**
     * This method allows the program to write a TXT file that will be used when concatenating two or more videos.
     * @param inputFiles The path to the input files to be concatenated
     * @param tempFile The path to the file that wil contain the paths of the files to be merged
     * @return The File instance of the file containing the paths that were passed to this method
     * @throws IOException if an I/O error occurred
     */
    private @NotNull File writeTXTFile(@NotNull List<String> inputFiles, @NotNull String tempFile) throws IOException {
        if(inputFiles == null || tempFile == null) {
            throw new IllegalArgumentException("No arguments to this method can be null.");
        }
        File file = new File(tempFile);
        if(file.exists()) {
            file.delete();
        }
        boolean created = file.createNewFile();
        for(String s: inputFiles) {
            if(s == null || s.equals("")) {
                file.delete();
                throw new IllegalArgumentException("No arguments to this method can be null or empty strings.");
            }
            FileUtils.writeStringToFile(file, "file '" + s.replace('\\', '/') + "'\n",
                    StandardCharsets.UTF_8, true);
        }

        return file;
    }

    /**
     * This method allows any object of this class to concatenate two or more videos whose path is given as input.
     * @param inputFiles The paths to the input files
     * @param time The maximum amount of time to wait for the video's creation
     * @param timeUnit The TimeUnit instance to be used
     * @param tempFile A temporary file used to store the paths of the files to be merged
     * @throws IOException if an I/O error occurs
     */
    public void mergeVideos(long time, @NotNull TimeUnit timeUnit, @NotNull List<String> inputFiles,
                            @NotNull String tempFile) throws IOException {
        File inputTXTFile = writeTXTFile(inputFiles, tempFile);
        builder.setCommand(builder.getCommand() + " -f concat -safe 0 -i \"" +
                inputTXTFile.getPath().replace('\\', '/') + "\"");
        if(streamCopy) {
            builder.setCommand(builder.getCommand() + " -c copy");
        }
        builder.addOutput(videoOutput);

        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(time, timeUnit);
    }
}
