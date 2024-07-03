package it.disi.unitn;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import it.disi.unitn.videocreator.TracksMerger;
import it.disi.unitn.videocreator.VideoCreator;
import it.disi.unitn.videocreator.transcoder.VideoTranscoder;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Builder class for convenience class FFMpeg
 */
public class FFMpegBuilder {
    //private String command;

    private final List<String> lcommand;

    /**
     * This constructor initializes the class with the path to ffmpeg's command line utility.
     * @param ffmpegPath The relative path to ffmpeg's command line utility. This parameter must not be null only if
     *                   the user is operating on a Windows Operating System.
     * @throws InvalidArgumentException If this constructor's argument is set to null and the user is running a Windows
     * Operating System
     */
    public FFMpegBuilder(@Nullable String ffmpegPath) throws InvalidArgumentException {
        lcommand = new ArrayList<>();
        if(SystemUtils.IS_OS_WINDOWS) {
            if(ffmpegPath == null || ffmpegPath.isEmpty()) {
                throw new InvalidArgumentException("The argument to this class's constructor cannot be null or an " +
                        "empty string.", "L'argomento fornito al costruttore di questa classe non puo' essere " +
                        "null o una stringa vuota.");
            }
            //command = ffmpegPath;
            lcommand.add(ffmpegPath);
        } else {
            //command = "ffmpeg";
            lcommand.add("ffmpeg");
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
        //return command;
        return String.join(" ", lcommand).trim().replaceAll("  +", " ");
    }

    /**
     * Adds a new flag to the command to be executed.
     * @param elem The flag to be added
     * @throws InvalidArgumentException If the given argument is null or an empty string
     */
    public void add(@NotNull String elem) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(elem)) {
            throw new InvalidArgumentException("An attempt to add a null or empty argument to the FFmpeg command was made.",
                    "E' stato effettuato un tentativo di aggiungere un argomento null o una stringa vuota al comando " +
                            "FFmpeg.");
        }
        lcommand.add(elem);
    }

    /**
     * This method adds the given string at the given position inside the FFmpeg command.
     * @param index The given position
     * @param elem The string to be inserted
     * @throws InvalidArgumentException If the second argument is null or an empty string
     */
    public void add(int index, @NotNull String elem) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(elem)) {
            throw new InvalidArgumentException("An attempt to add a null or empty argument to the FFmpeg command was made.",
                    "E' stato effettuato un tentativo di aggiungere un argomento null o una stringa vuota al comando " +
                            "FFmpeg.");
        }
        lcommand.add(index, elem);
    }

    /**
     * Convenience method to get the FFmpeg command to be executed as a List instance.
     * @return The FFmpeg command to be executed as a List instance
     */
    public List<String> getLCommand() {
        return lcommand;
    }

    /**
     * This method allows the user to reset the command of this Builder.
     * @param pathToFFMpeg The path to the ffmpeg executable. This parameter must not be null only if the user is operating
     * on a Windows Operating System.
     * @throws InvalidArgumentException If the user is operating on a Windows Operating System and the parameter of
     * this method is null.
     * @throws UnsupportedOperatingSystemException If the user is operating on an operating system that is not supported
     * by this library (i.e., all operating systems other than Windows and Linux OSs).
     */
    public void resetCommand(@Nullable String pathToFFMpeg) throws InvalidArgumentException,
            UnsupportedOperatingSystemException {
        lcommand.clear();

        if(SystemUtils.IS_OS_WINDOWS) {
            if(pathToFFMpeg == null || pathToFFMpeg.isEmpty()) {
                throw new InvalidArgumentException("The argument to this method cannot be null or an empty string.",
                        "L'argomento fornito a questo metodo non puo' essere null o una stringa vuota.");
            } else {
                add("\"" + pathToFFMpeg + "\"");
            }
        } else {
            if(SystemUtils.IS_OS_LINUX) {
                add("ffmpeg");
            } else {
                throw new UnsupportedOperatingSystemException();
            }
        }
    }

    /**
     * Adds the path to an input file to the command to be executed.
     * @param inputFile The input file whose path is to be added to the command
     * @throws InvalidArgumentException If the argument given to this method is null or an empty string
     */
    public void addInput(@NotNull String inputFile) throws InvalidArgumentException {
        add("-i \"" + inputFile + "\"");
    }

    /**
     * Create a new TracksMerger instance given a video output file and an audio and a video input file.
     * @param outputFile The given video output file
     * @param audioInput The given audio input file
     * @param videoInput The given video input file
     * @return The newly created TracksMerger instance
     * @throws InvalidArgumentException If the TracksMerger's constructor throws this exception
     */
    public TracksMerger newTracksMerger(@NotNull String outputFile, @NotNull String audioInput, @NotNull String videoInput)
            throws InvalidArgumentException {
        return new TracksMerger(this, outputFile, audioInput, videoInput);
    }

    /**
     * This method performs the same action as newTracksMerger(String, String, String), but it should be used only when
     * creating the final output video.
     * @param outputFile The path to the output file
     * @return The newly created TracksMerger instance
     * @throws InvalidArgumentException If the TracksMerger's constructor throws this exception
     */
    public TracksMerger newTracksMerger(@NotNull String outputFile) throws InvalidArgumentException {
        return new TracksMerger(this, outputFile);
    }

    /**
     * Returns a new VideoCreator instance given the path to the output file, the path to the input folder and the
     * output file's extension.
     * @param outputFile The path to the output file
     * This path has to be ffmpeg-compatible, and it must include the file extensions.
     * @return A new VideoCreator instance
     * @throws InvalidArgumentException If at least one of the given arguments is null or an empty string
     */
    public VideoCreator newVideoCreator(@NotNull String outputFile) throws InvalidArgumentException {
        return new VideoCreator(this, outputFile);
    }

    /**
     * Returns a new VideoTranscoder instance given the path to the output file, the path to the input folder and the
     * output file's extension.
     * @param outputFile The path to the output file
     * @return A new VideoTranscoder instance
     * @throws InvalidArgumentException If at least one of the given arguments is null or an empty string
     */
    public VideoTranscoder newVideoTranscoder(@NotNull String outputFile)
            throws InvalidArgumentException {
        return new VideoTranscoder(this, outputFile);
    }

    /**
     * Same as for addInput(), but this can add multiple files. Throws NotEnoughArgumentsException if the input value is
     * null or if one of the input values is null.
     * @param inputFiles The list of paths of the input files to be added to the command
     * @throws InvalidArgumentException When one of the arguments given to this method is null
     */
    public void addAllInputs(@NotNull String @NotNull ... inputFiles) throws InvalidArgumentException {
        if(inputFiles == null || Arrays.stream(inputFiles).anyMatch(StringExt::checkNullOrEmpty)) {
            throw new InvalidArgumentException("None of the arguments given to this method can be null or an empty " +
                    "string.", "Nessuno degli argomenti forniti a questo metodo puo' essere null o una stringa vuota.");
        }

        for(String s: inputFiles) {
            add("-i \"" + s + "\"");
        }
    }

    /**
     * Same as for addInput(), but this can add multiple files. Throws NotEnoughArgumentsException if the input value is
     * null or if one of the input values is null.
     * @param inputFiles The list of paths of the input files to be added to the command
     * @throws InvalidArgumentException When the argument given to this method is null or contains a null value or an
     * empty string
     */
    public void addAllInputs(@NotNull List<String> inputFiles) throws InvalidArgumentException{
        if(inputFiles == null || inputFiles.contains(null) || inputFiles.contains("")) {
            throw new InvalidArgumentException("The given list of input files cannot be null or contain null or empty " +
                    "strings.", "La data lista di file in input non puo' essere null o contenere valori null o pari a " +
                    "stringhe vuote.");
        }

        String res = String.join(" -i ", inputFiles);
        add("-i " + res.trim().replaceAll("  +", " "));
    }

    /**
     * Adds the path of an output file to the command.
     * @param outputFile The path of the output file to be added
     * @throws InvalidArgumentException If the given argument is null or an empty string
     */
    public void addOutput(@NotNull String outputFile) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(outputFile)) {
            throw new InvalidArgumentException("An attempt to add a null or empty argument to the FFmpeg command to " +
                    "be executed was made.", "E' stato effettuato un tentativo di aggiungere un argomento null o una " +
                    "stringa vuota al comando FFmpeg da eseguire.");
        }
        add("-y \"" + outputFile + "\"");
    }
}
