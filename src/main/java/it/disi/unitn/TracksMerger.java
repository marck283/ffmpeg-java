package it.disi.unitn;

import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import org.jetbrains.annotations.NotNull;

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
    public void merge() throws NotEnoughArgumentsException {
        builder.addAllInputs(videoInput, audioInput);
        builder.setCommand(builder.getCommand() + " -map 0:v");
        if(streamCopy) {
            builder.setCommand(builder.getCommand() + " -c:v copy");
        }
        builder.setCommand(builder.getCommand() + " -c:a copy -map 1:a");
        builder.addOutput(videoOutput);
    }
}
