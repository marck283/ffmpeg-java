package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * This class represents a color matrix to be used in the scaling filter.
 */
public class ColorMatrix {

    private String name;

    private final Locale l;

    private void printMsg(@NotNull String msg, @NotNull String itmsg) {
        if(l == Locale.ITALIAN || l == Locale.ITALY) {
            System.err.println(itmsg);
        } else {
            System.err.println(msg);
        }
    }

    /**
     * This class's constructor.
     * @param name The name of the color matrix
     */
    public ColorMatrix(@NotNull String name) {
        l = Locale.getDefault();
        if(StringExt.checkNullOrEmpty(name)) {
            System.err.println((new InvalidArgumentException("The name of this color matrix cannot be null or an empty " +
                    "string.", "Il nome della matrice dei colori non puo' essere null o una stringa vuota.")).getMessage());
        }

        switch(name) {
            case "auto", "bt709", "fcc", "bt601", "bt470", "smpte170m", "smpte240m", "bt2020" -> this.name = name;
            default -> {
                System.err.println((new InvalidArgumentException("The name of this color matrix must be recognizable by " +
                        "FFmpeg.", "Il nome di questa matrice dei colori deve essere riconoscibile da FFmpeg.")).getMessage());
            }
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
