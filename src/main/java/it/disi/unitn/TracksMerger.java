package it.disi.unitn;

import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * A class that helps merge audio and video tracks.
 */
public class TracksMerger {
    private final String videoOutput, audioInput, videoInput;

    private final FFMpegBuilder builder;

    private boolean streamCopy;

    TracksMerger(@NotNull FFMpegBuilder builder, @NotNull String outputFile, @NotNull String audioInput,
                        @NotNull String videoInput)
            throws NotEnoughArgumentsException {
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
     * This method enables the stream-copy option.
     * @param val A boolean value used to denote whether the tracks should be stream-copied
     */
    public void streamCopy(boolean val) {
        streamCopy = val;
    }

    /**
     * This method sets the correct command to merge the audio and video tracks.
     * @throws NotEnoughArgumentsException when the video input path or audio input path is null
     */
    public void mergeAudioWithVideo() throws NotEnoughArgumentsException {
        builder.addAllInputs(videoInput, audioInput);
        builder.setCommand(builder.getCommand() + " -map 0:v");
        if(streamCopy) {
            builder.setCommand(builder.getCommand() + " -c:v copy");
        }
        builder.setCommand(builder.getCommand() + " -c:a copy -map 1:a");
        builder.addOutput(videoOutput);
    }

    /**
     * This method allows the program to write a TXT file that will be used when concatenating two or more videos.
     * @param inputFiles The path to the input files to be concatenated
     * @return The File instance of the file containing the paths that were passed to this method
     * @throws IOException if an I/O error occurred
     */
    private @NotNull File writeTXTFile(@NotNull List<String> inputFiles) throws IOException {
        if(inputFiles == null) {
            throw new IllegalArgumentException("No arguments to this method can be null.");
        }
        File file = new File("./src/main/resources/it/disi/unitn/input/txt/inputFile.txt");
        if(file.exists()) {
            file.delete();
        }
        boolean created = file.createNewFile();
        for(String s: inputFiles) {
            if(s == null || s.equals("")) {
                file.delete();
                throw new IllegalArgumentException("No arguments to this method can be null or empty strings.");
            }
            FileUtils.writeStringToFile(file, "file '" + s.replace('\\', '/') + "'\n", StandardCharsets.UTF_8, true);
        }

        return file;
    }

    /**
     * This method allows any object of this class to concatenate two or more videos whose path is given as input.
     * @param inputFiles The paths to the input files
     * @throws IOException if an I/O error occurs
     */
    public void mergeVideos(@NotNull List<String> inputFiles) throws IOException {
        File inputTXTFile = writeTXTFile(inputFiles);
        builder.setCommand(builder.getCommand() + " -f concat -safe 0 -i \"" +
                inputTXTFile.getPath().replace('\\', '/') + "\"");
        if(streamCopy) {
            builder.setCommand(builder.getCommand() + " -c copy ");
        }
        //builder.addOutput(videoOutput);
        builder.setCommand(builder.getCommand() + "\"" + videoOutput + "\"");
    }
}
