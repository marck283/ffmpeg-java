package it.disi.unitn;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import it.disi.unitn.lasagna.audiocreator.AudioFiltering;
import it.disi.unitn.videocreator.TracksMerger;
import it.disi.unitn.videocreator.VideoCreator;
import it.disi.unitn.videocreator.transcoder.VideoTranscoder;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Builder class for convenience class FFMpeg
 */
public final class FFMpegBuilder {

    private final List<String> lcommand;

    /**
     * The class's constructor.
     */
    public FFMpegBuilder() {
        lcommand = new ArrayList<>();
        lcommand.add("ffmpeg");
    }

    /**
     * Creates a new FFMpeg instance using the information given to this FFMpegBuilder instance.
     * @return The created FFMpeg instance
     */
    @Contract(value = " -> new", pure = true)
    public @NotNull FFMpeg build() {
        return new FFMpeg(this);
    }

    /**
     * Convenience method to get the ffmpeg command to be executed.
     * @return The command to be executed
     */
    public @NotNull String getCommand() {
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
     * This method allows the user to reset the command of this Builder.
     * @throws UnsupportedOperatingSystemException If the user is operating on an operating system that is not supported
     * by this library (i.e., all operating systems other than Windows and Linux OSs).
     */
    public void resetCommand() throws InvalidArgumentException,
            UnsupportedOperatingSystemException {
        lcommand.clear();

        if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_WINDOWS) {
            add("ffmpeg");
        } else {
            throw new UnsupportedOperatingSystemException();
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
     */
    @Contract("_, _, _ -> new")
    public @NotNull TracksMerger newTracksMerger(@NotNull String outputFile, @NotNull String audioInput, @NotNull String videoInput) {
        return new TracksMerger(this, outputFile, audioInput, videoInput);
    }

    /**
     * This method performs the same action as newTracksMerger(String, String, String), but it should be used only when
     * creating the final output video.
     * @param outputFile The path to the output file
     * @return The newly created TracksMerger instance
     */
    @Contract("_ -> new")
    public @NotNull TracksMerger newTracksMerger(@NotNull String outputFile) {
        return new TracksMerger(this, outputFile);
    }

    /**
     * Returns a new VideoCreator instance given the path to the output file, the path to the input folder and the
     * output file's extension. None of the values given to this method can be null.
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
     */
    @Contract("_ -> new")
    public @NotNull VideoTranscoder newVideoTranscoder(@NotNull String outputFile) {
        return new VideoTranscoder(this, outputFile);
    }

    /**
     * This method returns a new AudioFiltering instance.
     * @param outputFile The given output audio file.
     * @return A new AudioFiltering instance made using the given output audio file.
     * @throws InvalidArgumentException If the given audio file is null or an empty string
     */
    @Contract("_ -> new")
    public @NotNull AudioFiltering newAudioFiltering(@NotNull String outputFile) throws InvalidArgumentException {
        return new AudioFiltering(this, outputFile);
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
        if(inputFiles == null || inputFiles.stream().anyMatch(StringExt::checkNullOrEmpty)) {
            throw new InvalidArgumentException("The given list of input files cannot be null or contain null or empty " +
                    "strings.", "La data lista di file in input non puo' essere null o contenere valori null o pari a " +
                    "stringhe vuote.");
        }

        String first = "-i \"" + inputFiles.removeFirst();
        inputFiles.addFirst(first);

        String last = inputFiles.removeLast() + "\"";
        inputFiles.addLast(last);

        String res = String.join("\" -i \"", inputFiles);
        add(res.trim().replaceAll("  +", " "));
    }

    /**
     * This method removes the given parameter from the FFmpeg command this instance of FFMpegBuilder contains.
     * @param e The given parameter. This value cannot be null or an empty string.
     * @throws InvalidArgumentException If the given parameter's value is null or an empty string
     */
    public void removeParam(@NotNull String e) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(e)) {
            throw new InvalidArgumentException("The given parameter's value cannot be null or an empty string.", "Il " +
                    "valore del parametro fornito non puo' essere null o una stringa vuota.");
        }
        lcommand.remove(e);
    }

    /**
     * This method lets the user modify a parameter's value.
     * @param oldVal The chosen parameter's current value. This value cannot be null or an empty string.
     * @param newVal The chosen parameter's new value. This value cannot be null or an empty string.
     * @throws InvalidArgumentException If any of the given arguments is null or an empty string
     */
    public void modifyParam(@NotNull String oldVal, @NotNull String newVal) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(oldVal) || StringExt.checkNullOrEmpty(newVal)) {
            throw new InvalidArgumentException("None of the given arguments can be null or an empty string.", "Nessuno " +
                    "degli argomenti forniti puo' essere null o una stringa vuota.");
        }
        int index = lcommand.indexOf(oldVal);
        if(index != -1) {
            removeParam(oldVal);
            add(index, newVal);
        }
    }

    /**
     * This method allows the modification of all the parameters according to the given argument.
     * @param action The Consumer instance to be used in this method.
     */
    public void modifyParams(Consumer<? super String> action) {
        lcommand.forEach(action);
    }

    /**
     * This method changes the last value of the FFmpeg command this instace represents.
     * @param newVal The new last element. This value cannot be null or an empty string.
     * @throws InvalidArgumentException If the given value is null or an empty string
     */
    public void modifyLast(@NotNull String newVal) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(newVal)) {
            throw new InvalidArgumentException("The given argument cannot be null or an empty string.", "L'argomento " +
                    "fornito non puo' essere null o una stringa vuota.");
        }
        add(lcommand.size() - 1, newVal);
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
