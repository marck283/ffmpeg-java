package it.disi.unitn.transitions.translation;

import org.jetbrains.annotations.NotNull;

public class TranslationTransition {

    private final Translation translation;

    public TranslationTransition(@NotNull String inputFile, @NotNull String tempOutDir) {
        translation = new Translation(inputFile, tempOutDir);
    }

}
