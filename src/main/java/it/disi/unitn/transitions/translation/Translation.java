package it.disi.unitn.transitions.translation;

import it.disi.unitn.transitions.Transition;
import org.jetbrains.annotations.NotNull;

class Translation extends Transition {

    Translation(@NotNull String inputFile, @NotNull String tempOutDir) {
        super(inputFile, tempOutDir);
    }

}