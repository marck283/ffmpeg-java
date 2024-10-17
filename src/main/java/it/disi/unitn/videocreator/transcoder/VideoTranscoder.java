package it.disi.unitn.videocreator.transcoder;

import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.VideoCreator;
import org.jetbrains.annotations.NotNull;

//import java.util.List;

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
     */
    public VideoTranscoder(@NotNull FFMpegBuilder builder, @NotNull String outputFile) {
        super(builder, outputFile);
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
     */
    private void modifyParams(@NotNull String param) {
        builder.modifyParams(e -> {
            if((param.equals("vs") && e.startsWith("-c:v")) || (param.equals("as") && e.startsWith("-c:a"))) {
                try {
                    builder.modifyParam(e, e + " copy");
                } catch (InvalidArgumentException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
    }

    /**
     * This method sets up the correct track extraction flag.
     * @param whichTrack A String parameter that indicates which track is to be extracted. The only admissible values are
     *                   "video" and "audio"
     * @throws InvalidArgumentException If the given argument to this method is null or an empty string
     */
    private void readyUpForTrackExtraction(@NotNull String whichTrack) throws InvalidArgumentException {
        /*List<String> outList = builder.getLCommand();
        int finInd = outList.indexOf(outList.getLast());*/
        if(whichTrack.equals("video")) {
            //Video track is extracted
            builder.modifyLast("-an");
            //builder.add(finInd, "-an");
        } else {
            //Audio track is extracted
            builder.modifyLast("-vn");
            //builder.add(finInd, "-vn");
        }
    }

    /**
     * This method will create the FFmpeg command which can then be used to run the FFmpeg process and produce the desired
     * result.
     */
    public void createCommand() {
        super.createCommand();

        try {
            if(videoStreamCopy) {
                modifyParams("vs");
            } else {
                if(extractVideo) {
                    readyUpForTrackExtraction("video");
                }
            }

            if(audioStreamCopy) {
                modifyParams("va");
            } else {
                if(extractAudio) {
                    readyUpForTrackExtraction("audio");
                }
            }
        } catch (InvalidArgumentException ex) {
            System.err.println(ex.getMessage());
            //ex.printStackTrace();
            System.exit(1);
        }
    }

}
