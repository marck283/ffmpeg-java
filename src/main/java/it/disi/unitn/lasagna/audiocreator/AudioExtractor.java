package it.disi.unitn.lasagna.audiocreator;

import it.disi.unitn.FFMpegBuilder;
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
     * @param builder    The FFMpegBuilder instance
     * @param outputFile The output file path. This value cannot be null or an empty string.
     */
    public AudioExtractor(@NotNull FFMpegBuilder builder, @NotNull String outputFile, @NotNull String inputFile)
            throws InvalidArgumentException {
        if(builder == null || outputFile == null || inputFile == null) {
            throw new InvalidArgumentException("None of the arguments given to this constructor can be null.", "Nessuno " +
                    "degli argomenti forniti a questo costruttore puo' essere null.");
        }
        this.ffmpegBuilder = builder;
        this.outputFile = outputFile;
        this.inputFile = inputFile;
    }

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
