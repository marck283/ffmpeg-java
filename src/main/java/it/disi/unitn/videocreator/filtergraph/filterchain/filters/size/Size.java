package it.disi.unitn.videocreator.filtergraph.filterchain.filters.size;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

/**
 * This class is a utility class that can be used to check if the given size ID is allowed by FFmpeg.
 */
public class Size {

    private String sizeID;

    /**
     * This class's constructor.
     */
    public Size() {
        sizeID = "";
    }

    /**
     * This method checks if the size's ID is allowed by FFmpeg.
     * @param sizeID The given size ID. This value cannot be null or an empty string.
     * @return True if the given value is allowed by FFmpeg, false otherwise.
     */
    public boolean checkSizeID(@NotNull String sizeID) {
        return switch(sizeID) {
            case "ntsc", "pal", "qntsc", "qpal", "sntsc", "spal", "film", "ntsc-film", "sqcif", "qcif", "cif", "4cif",
                 "16cif", "qqvga", "qvga", "vga", "svga", "xga", "uxga", "qxga", "sxga", "qsxga", "hsxga", "wvga",
                 "wxga", "wsxga", "wuxga", "woxga", "wqsxga", "wquxga", "whsxga", "whuxga", "cga", "ega", "hd480",
                 "hd720", "hd1080", "2k", "2kflat", "2kscope", "4k", "4kflat", "4kscope", "nhd", "hqvga", "wqvga",
                 "fwqvga", "hvga", "qhd", "2kdci", "4kdci", "uhd2160", "uhd4320" -> true;
            default -> false;
        };
    }

    /**
     * This method sets the size's ID after checking if it is accepted by FFmpeg.
     * @param sizeID The size ID's value. This value cannot be null or an empty string, and it must be accepted by FFmpeg.
     * @throws InvalidArgumentException If the given value is null or an empty string.
     */
    public void setSizeID(@NotNull String sizeID) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(sizeID)) {
            throw new InvalidArgumentException("The size's ID cannot be null or empty.", "L'ID della dimensione non puo' " +
                    "essere null o una stringa vuota.");
        }

        if(checkSizeID(sizeID)) {
            this.sizeID = sizeID;
        } else {
            this.sizeID = "";
        }
    }

    /**
     * This method returns the given size ID.
     * @return The given size ID.
     */
    public String getSizeID() {
        return sizeID;
    }
}
