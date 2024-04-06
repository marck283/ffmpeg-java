package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.aformat;

import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class implements FFmpeg's channel layouts.
 */
public class ChannelLayout {

    private String chID;

    private final List<String> chidList;

    /**
     * The class's constructor.
     */
    public ChannelLayout() {
        chidList = new ArrayList<>();
    }

    /**
     * Sets the channel layout.
     * @param chID The given channel layout. This is either the channel ID or one of the channel layouts recognized by FFmpeg
     * @throws InvalidArgumentException If the given channel layout does not match any of the values accepted by FFmpeg
     * @throws UnsupportedOperationException If the user already specified a channel ID by calling addChannelID()
     */
    public void setChannelID(@NotNull String chID) throws InvalidArgumentException {
        if(chidList.isEmpty()) {
            switch(chID) {
                case "mono", "stereo", "2.1", "3.0", "3.0(back)", "4.0", "quad", "quad(side)", "3.1", "5.0", "5.0(side)",
                        "4.1", "5.1", "5.1(side)", "6.0", "6.0(front)", "3.1.2", "hexagonal", "6.1", "6.1(front)", "7.0",
                        "7.0(front)", "7.1", "7.1(wide)", "7.1(wide-side)", "5.1.2", "octagonal", "cube", "5.1.4", "7.1.2",
                        "7.1.4", "7.2.3", "9.1.4", "hexadecagonal", "downmix", "22.2" -> this.chID = chID;
                default -> throw new InvalidArgumentException("The given channel ID cannot be null, an empty string or any value not " +
                        "recognized by FFmpeg.", "Il valore fonito per l'id del canale audio non puo' essere null, una stringa " +
                        "vuota o un qualunque valore non riconosciuto da FFmpeg.");
            }
        } else {
            Locale l = Locale.getDefault();
            if(l == Locale.ITALIAN || l == Locale.ITALY) {
                System.err.println("Non e' possibile impostare una combinazione di ID dei canali audio se la lista dei " +
                        "canali audio non e' vuota.");
            } else {
                System.err.println("The list of audio channel IDs must be empty in order to set a combination of audio " +
                        "channels.");
            }
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Adds the ID of a single audio channel.
     * @param channelID The given audio channel's ID
     * @throws InvalidArgumentException If the given channel ID is null, an empty string or any value not recognized by
     * FFmpeg
     * @throws UnsupportedOperationException If the user already specified a combination of audio channels by calling
     * setChannelID()
     */
    public void addChannelID(@NotNull String channelID) throws InvalidArgumentException {
        if(chID == null || chID.isEmpty()) {
            switch(channelID) {
                case "FL", "FR", "FC", "LFE", "BL", "BR", "FLC", "FRC", "BC", "SL", "SR", "TC", "TFL", "TFC", "TFR", "TBL",
                        "TBC", "TBR", "DL", "DR", "WL", "WR", "SDL", "SDR", "LFE2" -> this.chID = channelID;
                default -> throw new InvalidArgumentException("The given channel ID cannot be null, an empty string or any value not " +
                        "recognized by FFmpeg.", "Il valore fonito per l'id del canale audio non puo' essere null, una stringa " +
                        "vuota o un qualunque valore non riconosciuto da FFmpeg.");
            }
        } else {
            Locale l = Locale.getDefault();
            if(l == Locale.ITALIAN || l == Locale.ITALY) {
                System.err.println("La combinazione di canali audio deve essere null o una stringa vuota per poter aggiungere " +
                        "l'ID di un singolo canale audio.");
            } else {
                System.err.println("The combination of audio channels must be null or an empty string in order to add the " +
                        "ID of a single audio channel.");
            }
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Gets the list of channel IDs in this ChannelLayout instance.
     * @return The list of channel IDs in this ChannelLayout instance
     */
    public List<String> getChannels() {
        return chidList;
    }

    @Override
    public String toString() {
        if(chidList.isEmpty()) {
            return chID;
        }
        return String.join("+", chidList);
    }

}
