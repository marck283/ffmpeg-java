package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a color matrix to be used in the scaling filter.
 */
public class ColorMatrix {

    private final String name;

    /**
     * This class's constructor.
     * @param name The name of the color matrix
     * @throws InvalidArgumentException If the given color matrix's name is null, an empty string or a value not recognized
     * by FFmpeg
     */
    public ColorMatrix(@NotNull String name) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(name)) {
            throw new InvalidArgumentException("The name of this color matrix cannot be null or an empty string.", "Il " +
                    "nome della matrice dei colori non puo' essere null o una stringa vuota.");
        }

        switch(name) {
            case "auto", "bt709", "fcc", "bt601", "bt470", "smpte170m", "smpte240m", "bt2020" -> this.name = name;
            default -> throw new InvalidArgumentException("The name of this color matrix must be recognizable by FFmpeg.",
                    "Il nome di questa matrice dei colori deve essere riconoscibile da FFmpeg.");
        }
    }

    /**
     * Gets this color matrix's name.
     * @return The name of this color matrix
     */
    public String getName() {
        return name;
    }

}
