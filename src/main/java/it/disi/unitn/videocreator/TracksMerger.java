package it.disi.unitn.videocreator;

import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * A class that helps merge audio and video tracks.
 */
public class TracksMerger extends VideoCreator {
    private final String videoOutput, audioInput, videoInput;

    private final FFMpegBuilder builder;

    private boolean streamCopy;

    /**
     * The class's constructor.
     * @param builder The FFMpegBuilderInstance
     * @param outputFile The path to the output video
     * @param inputFolder The path to the input folder
     * @param pattern The name of the file to be included in the video
     * @param audioInput The path to the input audio file
     * @param videoInput The path to the input video file
     * @throws NotEnoughArgumentsException If any of the arguments given to this method is null or an empty string
     */
    public TracksMerger(@NotNull FFMpegBuilder builder, @NotNull String outputFile, @NotNull String inputFolder, @NotNull String pattern,
                 @NotNull String audioInput, @NotNull String videoInput) throws NotEnoughArgumentsException {
        super(builder, outputFile, inputFolder, pattern);
        if(audioInput == null || audioInput.isEmpty() || videoInput == null || videoInput.isEmpty()) {
            throw new NotEnoughArgumentsException("The arguments to this class's constructor cannot be null or empty " +
                    "strings.", "Nessuno degli argomenti forniti al costruttore di questa classe puo' essere null o una " +
                    "stringa vuota.");
        }

        this.builder = builder;
        this.audioInput = audioInput;
        this.videoInput = videoInput;
        videoOutput = outputFile;
    }

    /**
     * The class's constructor. This constructor should only be called when the program is about to create the output
     * video.
     * @param builder The FFMpegBuilderInstance
     * @param outputVideo The path to the output video
     * @param inputFolder The path to the input folder
     * @param pattern The name of the file to be included in the video
     * @throws NotEnoughArgumentsException If any of the arguments given to this method is null or an empty string
     */
    public TracksMerger(@NotNull FFMpegBuilder builder, @NotNull String outputVideo, @NotNull String inputFolder, @NotNull String pattern)
            throws NotEnoughArgumentsException {
        super(builder, outputVideo, inputFolder, pattern);

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
     * @throws IOException If an I/O error occurs
     * @throws InvalidArgumentException If the given timeout is negative or the TimeUnit instance is null
     */
    public void mergeAudioWithVideo(long time, @NotNull TimeUnit timeUnit) throws NotEnoughArgumentsException, IOException,
            InvalidArgumentException {
        builder.addAllInputs(videoInput, audioInput);
        builder.add("-map 0:v");
        //builder.setCommand(builder.getCommand() + " -map 0:v");
        if(streamCopy) {
            builder.add("-c:v copy");
            //builder.setCommand(builder.getCommand() + " -c:v copy");
        }
        builder.add("-c:a copy -map 1:a");
        //builder.setCommand(builder.getCommand() + " -c:a copy -map 1:a");
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
        File file = new File(tempFile);
        if(file.exists()) {
            file.delete();
        }
        boolean created = file.createNewFile();
        if(!created) {
            Locale l = Locale.getDefault();
            if(l == Locale.ITALIAN || l == Locale.ITALY) {
                System.err.println("Impossibile creare il file " + tempFile);
            } else {
                System.err.println("Cannot create file " + tempFile);
            }
            System.exit(1);
        }
        for(String s: inputFiles) {
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
     * @throws NotEnoughArgumentsException If any of the given arguments is less than or equal to zero, null or an empty
     * string or contains null or empty strings
     * @throws InvalidArgumentException If the given timeout is negative or the given TimeUnit instance is null
     */
    public void mergeVideos(long time, @NotNull TimeUnit timeUnit, @NotNull List<String> inputFiles,
                            @NotNull String tempFile) throws IOException, NotEnoughArgumentsException, InvalidArgumentException {
        //Da riscrivere rispettando le specifiche sopra fornite
        if(time <= 0 || timeUnit == null || inputFiles == null || inputFiles.stream().anyMatch(s -> s == null || s.isEmpty()) ||
        tempFile == null || tempFile.isEmpty()) {
            throw new NotEnoughArgumentsException("No argument to this method can be null, less than or equal to zero or " +
                    " an empty string, nor can it contain null or empty strings.", "Nessuno degli argomenti forniti a " +
                    "questo metodo puo' essere null, minore o uguale a zero o una stringa vuota o contenere stringhe null " +
                    "o vuote.");
        }
        File inputTXTFile = writeTXTFile(inputFiles, tempFile);
        builder.add("-f concat -safe 0 -i \"" +
                inputTXTFile.getPath().replace('\\', '/') + "\"");
        /*builder.setCommand(builder.getCommand() + " -f concat -safe 0 -i \"" +
                inputTXTFile.getPath().replace('\\', '/') + "\"");*/
        if(streamCopy) {
            builder.add("-c copy");
            //builder.setCommand(builder.getCommand() + " -c copy");
        }
        builder.addOutput(videoOutput);

        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(time, timeUnit); //"time" and "timeUnit" are never null here.
    }
}
