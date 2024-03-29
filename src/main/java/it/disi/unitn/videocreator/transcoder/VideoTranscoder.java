package it.disi.unitn.videocreator.transcoder;

import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import it.disi.unitn.videocreator.VideoCreator;
import org.jetbrains.annotations.NotNull;

/**
 * This class performs the video transcoding operations.
 */
public class VideoTranscoder extends VideoCreator {

    private boolean audioStreamCopy, videoStreamCopy, extractVideo, extractAudio;

    private final FFMpegBuilder builder;

    /**
     * The class's constructor.
     * @param builder The FFMpegBuilder instance
     * @param outputFile The path to the output file
     * @param inputFolder The path to the input folder
     * @param fileExtension The output file extension
     * @throws NotEnoughArgumentsException If at least one of the given arguments is null or an empty string
     */
    public VideoTranscoder(@NotNull FFMpegBuilder builder, @NotNull String outputFile, @NotNull String inputFolder,
                           @NotNull String fileExtension) throws NotEnoughArgumentsException {
        super(builder, outputFile, inputFolder, fileExtension);
        audioStreamCopy = false;
        videoStreamCopy = false;
        extractVideo = false;
        extractAudio = false;
        this.builder = builder;
    }

    /**
     * This method enables the stream-copying of the audio track
     */
    public void enableAudioStreamCopy() {
        audioStreamCopy = true;
    }

    /**
     * This method enables the stream-copying of the video track
     */
    public void enableVideoStreamCopy() {
        videoStreamCopy = true;
    }

    /**
     * This method disables the stream-copying of the audio track
     */
    public void disableAudioStreamCopy() {
        audioStreamCopy = false;
    }

    /**
     * This method disables the stream-copying of the video track
     */
    public void disableVideoStreamCopy() {
        videoStreamCopy = false;
    }

    /**
     * This method enables the extraction of the video track
     */
    public void enableVideoExtraction() {
        extractVideo = true;
    }

    /**
     * This method enables the extraction of the audio track
     */
    public void enableAudioExtraction() {
        extractAudio = true;
    }

    /**
     * This method disables the extraction of the video track
     */
    public void disableVideoExtraction() {
        extractVideo = false;
    }

    /**
     * This method disables the extraction of the audio track
     */
    public void disableAudioExtraction() {
        extractAudio = false;
    }

    /**
     * This method allows the program to remove the unnecessary parameters from the FFmpeg command which will be executed.
     * @param param This is a parameter that indicates whether this method is being called to deal with video or audio
     *              parameters
     * @throws NotEnoughArgumentsException If the argument given to this method is null or an empty string
     */
    private void removeParams(@NotNull String param) throws NotEnoughArgumentsException {
        if(param == null || param.isEmpty()) {
            throw new NotEnoughArgumentsException("The given argument is null or an empty string.", "L'argomento fornito " +
                    "e' null o una stringa vuota.");
        }
        builder.getLCommand().forEach(e -> {
            if((param.equals("vs") && e.startsWith("-c:v")) || (param.equals("as") && e.startsWith("-c:a"))) {
                int index = builder.getLCommand().indexOf(e);
                builder.getLCommand().remove(e);
                try {
                    builder.add(index, e + " copy");
                } catch (NotEnoughArgumentsException ex) {
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        });
    }

    /**
     * This method sets up the correct track extraction flag.
     * @param whichTrack A String parameter that indicates which track is to be extracted. The only admissible values are
     *                   "video" and "audio"
     * @throws NotEnoughArgumentsException If the given argument to this method is null or an empty string
     */
    private void readyUpForTrackExtraction(@NotNull String whichTrack) throws NotEnoughArgumentsException {
        if(whichTrack == null || whichTrack.isEmpty()) {
            throw new NotEnoughArgumentsException("The given argument is null or an empty string.", "L'argomento fornito " +
                    "e' null o una stringa vuota.");
        }

        String output = builder.getLCommand().removeLast();
        if(whichTrack.equals("video")) {
            //Video track is extracted
            builder.add("-an");
        } else {
            //Audio track is extracted
            builder.add("-vn");
        }
        builder.add(output);
    }

    /**
     * This method will create the FFmpeg command which can then be used to run the FFmpeg process and produce the desired
     * result.
     * @throws InvalidArgumentException If either the user wants to stream-copy the audio or video track orto extract the
     * video track (therefore creating a new video) and the video size ID or the width and the height parameters
     * are not set accordingly
     */
    public void createCommand() throws InvalidArgumentException {
        super.createCommand(videoStreamCopy || extractVideo || audioStreamCopy);

        try {
            if(videoStreamCopy) {
                removeParams("vs");
            } else {
                if(extractVideo) {
                    readyUpForTrackExtraction("video");
                }
            }

            if(audioStreamCopy) {
                removeParams("va");
            } else {
                if(extractAudio) {
                    readyUpForTrackExtraction("audio");
                }
            }
        } catch (NotEnoughArgumentsException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
    }

}
