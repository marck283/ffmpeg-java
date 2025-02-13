package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.aformat;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.MultiLanguageUnsupportedOperationException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class implements FFmpeg's channel layouts.
 */
final public class ChannelLayout {

    private String chID;

    private final List<String> chidList;

    /**
     * The class's constructor.
     */
    public ChannelLayout() {
        chidList = new ArrayList<>();
    }

    /**
     * The class's constructor given a channel's ID.
     * @param id The given channel's ID
     */
    public ChannelLayout(@NotNull String id) {
        if(StringExt.checkNullOrEmpty(id)) {
            Locale l = Locale.getDefault();
            if(l == Locale.ITALY || l == Locale.ITALIAN) {
                System.err.println("L'ID del canale fornito non puo' essere null o una stringa vuota.");
            } else {
                System.err.println("The given channel's ID cannot be null or an empty string.");
            }
            System.exit(1);
        }
        chID = id;
        chidList = new ArrayList<>();
    }

    /**
     * Sets the channel layout.
     *
     * @param chID The given channel layout. This is either the channel ID or one of the channel layouts recognized by FFmpeg
     * @throws InvalidArgumentException      If the given channel layout does not match any of the values accepted by FFmpeg
     * @throws MultiLanguageUnsupportedOperationException If the user already specified a channel ID by calling addChannelID()
     */
    public void setChannelID(@NotNull String chID) throws InvalidArgumentException, MultiLanguageUnsupportedOperationException {
        if (chidList.isEmpty()) {
            switch (chID) {
                case "mono", "stereo", "2.1", "3.0", "3.0(back)", "4.0", "quad", "quad(side)", "3.1", "5.0",
                     "5.0(side)",
                     "4.1", "5.1", "5.1(side)", "6.0", "6.0(front)", "3.1.2", "hexagonal", "6.1", "6.1(front)", "7.0",
                     "7.0(front)", "7.1", "7.1(wide)", "7.1(wide-side)", "5.1.2", "octagonal", "cube", "5.1.4", "7.1.2",
                     "7.1.4", "7.2.3", "9.1.4", "hexadecagonal", "downmix", "22.2" -> this.chID = chID;
                default ->
                        throw new InvalidArgumentException("The given channel ID cannot be null, an empty string or any value not " +
                                "recognized by FFmpeg.", "Il valore fonito per l'id del canale audio non puo' essere null, una stringa " +
                                "vuota o un qualunque valore non riconosciuto da FFmpeg.");
            }
        } else {
            MultiLanguageUnsupportedOperationException.throwUnsupportedOperationException("The list of audio channel IDs must be " +
                    "empty in order to set a combination of audio channels.", "Non e' possibile impostare una " +
                    "combinazione di ID dei canali audio se la lista dei canali audio non e' vuota.");
        }
    }

    /**
     * Adds the ID of a single audio channel.
     *
     * @param channelID The given audio channel's ID
     * @throws InvalidArgumentException      If the given channel ID is null, an empty string or any value not recognized by
     *                                       FFmpeg
     * @throws MultiLanguageUnsupportedOperationException If the user already specified a combination of audio channels by calling
     *                                       setChannelID()
     */
    public void addChannelID(@NotNull String channelID) throws InvalidArgumentException, MultiLanguageUnsupportedOperationException {
        if (StringExt.checkNullOrEmpty(chID)) {
            switch (channelID) {
                case "FL", "FR", "FC", "LFE", "BL", "BR", "FLC", "FRC", "BC", "SL", "SR", "TC", "TFL", "TFC", "TFR",
                     "TBL",
                     "TBC", "TBR", "DL", "DR", "WL", "WR", "SDL", "SDR", "LFE2", "stereo", "quad" ->
                        chidList.add(channelID);
                default ->
                        throw new InvalidArgumentException("The given channel ID cannot be null, an empty string or any value not " +
                                "recognized by FFmpeg.", "Il valore fonito per l'id del canale audio non puo' essere null, una stringa " +
                                "vuota o un qualunque valore non riconosciuto da FFmpeg.");
            }
        } else {
            MultiLanguageUnsupportedOperationException.throwUnsupportedOperationException("The combination of audio channels must be " +
                    "null or an empty string in order to add the ID of a single audio channel.", "La combinazione di " +
                    "canali audio deve essere null o una stringa vuota per poter aggiungere l'ID di un singolo canale " +
                    "audio.");
        }
    }

    /**
     * This method adds all channels from the given ChannelLayout instance.
     * @param chlay The given ChannelLayout instance. This value cannot be null.
     * @throws InvalidArgumentException If the given ChannelLayout instance is null
     */
    public void addChannels(@NotNull ChannelLayout chlay) throws InvalidArgumentException {
        if(chlay == null) {
            throw new InvalidArgumentException("The given ChannelLayout instance cannot be null.", "L'istanza di " +
                    "ChannelLayout fornita non puo' essere null.");
        }
        chidList.addAll(chlay.chidList);
    }

    /**
     * This method returns true if this ChannelLayout's channel id's list is empty, otherwise it returns false.
     * @return True if this ChannelLayout's channel id's list is empty, otherwise it returns false.
     */
    public boolean isEmpty() {
        return chidList.isEmpty();
    }

    @Override
    public String toString() {
        if (chidList.isEmpty()) {
            return chID;
        }
        return String.join("+", chidList);
    }

    /**
     * Gets the list of channel IDs in this ChannelLayout instance. The elements are separated by a "|" symbol.
     *
     * @return The list of channel IDs in this ChannelLayout instance
     */
    public String toStringBar() {
        if (chidList.isEmpty()) {
            return chID;
        }
        return String.join("|", chidList);
    }

}
