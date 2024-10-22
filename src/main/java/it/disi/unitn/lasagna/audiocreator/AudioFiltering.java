package it.disi.unitn.lasagna.audiocreator;

import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.MultiLanguageUnsupportedOperationException;
import it.disi.unitn.videocreator.filtergraph.AudioFilterGraph;
import it.disi.unitn.videocreator.filtergraph.FilterGraph;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This class makes audio filtering possible when only an audio track is processed.
 */
public class AudioFiltering {

    private FilterGraph afg;

    private final FFMpegBuilder builder;

    private final List<String> pattern;

    private final String outputFile;

    /**
     * The class's constructor.
     * @param builder The FFMpegBuilder instance
     * @param outputFile The output file path. This value cannot be null or an empty string.
     */
    public AudioFiltering(@NotNull FFMpegBuilder builder, @NotNull String outputFile) {
        afg = new AudioFilterGraph();
        this.builder = builder;
        pattern = new ArrayList<>();

        if(StringExt.checkNullOrEmpty(outputFile)) {
            System.err.println((new InvalidArgumentException("Cannot give FFmpeg an output file represented by a null value or an empty " +
                    "string.", "Non e' possibile fornire a FFmpeg un file di output identificato da un valore null o da " +
                    "una stringa vuota.")).getMessage());
            System.exit(10);
        }
        this.outputFile = outputFile;
    }

    /**
     * This method sets the AudioFilterGraph on the audio track.
     * @param afg The given AudioFilterGraph. This parameter cannot be null.
     * @throws InvalidArgumentException If the given parameter is null
     */
    public void setAudioFilterGraph(@NotNull AudioFilterGraph afg) throws InvalidArgumentException {
        if (afg == null) {
            throw new InvalidArgumentException("The audio filter graph must not be null.", "Il grafo del filtro audio " +
                    "non puo' essere null.");
        }

        this.afg = afg;
    }

    /**
     * This method adds an input audio file to the list of already given audio files.
     * @param input The input file to be added
     * @throws InvalidArgumentException If the given input value is null or an empty string or the given file does not
     *                                  exist
     * @throws MultiLanguageUnsupportedOperationException If the input value already includes all files in
     *                                  the folder
     */
    public void addInput(@NotNull String input) throws InvalidArgumentException, MultiLanguageUnsupportedOperationException {
        if (StringExt.checkNullOrEmpty(input)) {
            throw new InvalidArgumentException("The given input file cannot be null or an empty string.", "Il file fornito " +
                    "non puo' essere null o una stringa vuota.");
        }

        if(pattern.stream().anyMatch(e -> e.contains("*."))) {
            MultiLanguageUnsupportedOperationException.throwUnsupportedOperationException("Cannot insert another input value when " +
                    "there is already another one that includes all files in the same folder.", "Non e' possibile " +
                    "inserire un altro valore di input quando ne e' gia' presente uno che comprenda tutti i file " +
                    "presenti nella cartella.");
        }

        Path p = Paths.get(input);
        if (!input.startsWith("*.") && !Files.exists(p)) {
            throw new InvalidArgumentException("The given file does not exist.", "Il file fornito non esiste.");
        }

        pattern.add(input);
    }

    /**
     * This method creates the FFmpeg command to apply filters to the given audio files.
     * @throws InvalidArgumentException If
     */
    public void createCommand() throws InvalidArgumentException, MultiLanguageUnsupportedOperationException {
        if(afg == null) {
            MultiLanguageUnsupportedOperationException.throwUnsupportedOperationException("No AudioFilterGraph to apply to input files " +
                    "was given.", "Non e' stato fornito alcun AudioFilterGraph da applicare ai file in input.");
        }
        builder.addAllInputs(pattern);
        builder.add(afg.toString());
        builder.addOutput(outputFile);
    }

}
