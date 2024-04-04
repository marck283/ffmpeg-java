package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.aformat;

import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

public class ChannelLayout {

    private String chID;

    /**
     * Sets the channel layout.
     * @param chID The given channel layout. This i either the channel ID or one of the channel layouts recognized by FFmpeg
     * @throws InvalidArgumentException If the given channel layout does not match any of the values accepted by FFmpeg
     */
    public void setChannelID(@NotNull String chID) throws InvalidArgumentException {
        switch(chID) {
            case "FL", "FR", "FC", "LFE", "BL", "BR", "FLC", "FRC", "BC", "SL", "SR", "TC", "TFL", "TFC", "TFR", "TBL",
                    "TBC", "TBR", "DL", "DR", "WL", "WR", "SDL", "SDR", "LFE2", "mono", "stereo", "2.1", "3.0", "3.0(back)",
                    "4.0", "quad", "quad(side)", "3.1", "5.0", "5.0(side)", "4.1", "5.1", "5.1(side)", "6.0", "6.0(front)",
                    "3.1.2", "hexagonal", "6.1", "6.1(front)", "7.0", "7.0(front)", "7.1", "7.1(wide)", "7.1(wide-side)",
                    "5.1.2", "octagonal", "cube", "5.1.4", "7.1.2", "7.1.4", "7.2.3", "9.1.4", "hexadecagonal", "downmix",
                    "22.2" -> this.chID = chID;
            default -> throw new InvalidArgumentException("The given channel ID cannot be null, an empty string or any value not " +
                    "recognized by FFmpeg.", "Il valore fonito per l'id del canale audio non puo' essere null, una stringa " +
                    "vuota o un qualunque valore non riconosciuto da FFmpeg.");
        }
    }

    //Da rivedere
    @Override
    public String toString() {
        return chID;
    }

}
