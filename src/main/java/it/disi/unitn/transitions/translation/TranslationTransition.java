package it.disi.unitn.transitions.translation;

import org.jetbrains.annotations.NotNull;

/**
 * This class handles the translation of text.
 */
public class TranslationTransition {

    private final Translation translation;

    /**
     * This class's constructor.
     * @param inputFile The given input file. This value must not be null or an empty string.
     * @param tempOutDir The given output directory for the intermediate files. This value must not be null or an empty
     *                   string.
     */
    public TranslationTransition(@NotNull String inputFile, @NotNull String tempOutDir) {
        translation = new Translation(inputFile, tempOutDir);
    }

}
