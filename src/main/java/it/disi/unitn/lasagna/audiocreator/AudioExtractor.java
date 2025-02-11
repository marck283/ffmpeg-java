package it.disi.unitn.lasagna.audiocreator;

import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements an audio extractor.
 */
public class AudioExtractor {

    private final String inputFile, outputFile;

    private final FFMpegBuilder ffmpegBuilder;

    /**
     * The class's constructor.
     *
     * @param builder    The FFMpegBuilder instance. This parameter cannot be null.
     * @param outputFile The output file path. This value cannot be null or an empty string.
     * @param inputFile  The input file path. This value cannot be null or an empty string.
     * @throws InvalidArgumentException If any of the arguments given to this constructor is null or an empty string
     */
    public AudioExtractor(@NotNull FFMpegBuilder builder, @NotNull String outputFile, @NotNull String inputFile)
            throws InvalidArgumentException {
        if(builder == null || StringExt.checkNullOrEmpty(outputFile) || StringExt.checkNullOrEmpty(inputFile)) {
            throw new InvalidArgumentException("None of the arguments given to AudioExtractor's constructor can be null " +
                    "or an empty string.", "Nessuno degli argomenti forniti al costruttore di AudioExtractor puo' essere " +
                    "null o una stringa vuota.");
        }
        this.ffmpegBuilder = builder;
        this.outputFile = outputFile;
        this.inputFile = inputFile;
    }

    /**
     * This method creates the command that will be run to extract the audio track from the input file.
     */
    public void createCommand() {
        try {
            ffmpegBuilder.addInput(inputFile);
            ffmpegBuilder.add("-vn");
            ffmpegBuilder.addOutput(outputFile);
        } catch(InvalidArgumentException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException();
        }
    }
}
