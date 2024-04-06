package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.adrc;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.aformat.ChannelLayout;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements FFmpeg's "adrc" filter. ATTENTION: this class is still being tested.
 */
public class ADrc extends AudioFilter {

    private String transfer = "p";

    private int attack = 50, release = 100;

    private final ChannelLayout chLayout;

    /**
     * This class's constructor.
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public ADrc() throws InvalidArgumentException {
        super("adrc");
        chLayout = new ChannelLayout();
    }

    /**
     * Sets the transfer expression. THis value must be recognizable by FFmpeg.
     * @param val The given transfer expression
     * @throws InvalidArgumentException If the given transfer expression is null or an empty string
     */
    public void setTransfer(@NotNull String val) throws InvalidArgumentException {
        if(val == null || val.isEmpty()) {
            throw new InvalidArgumentException("The transfer expression cannot be null or an empty string.", "L'espressione " +
                    "di trasferimento non puo' essere null o una stringa vuota.");
        }

        transfer = val;
    }

    /**
     * Sets the attack value in milliseconds.
     * @param val The given attack value in milliseconds
     * @throws InvalidArgumentException If the given attack value is less than 1 or greater than 1000
     */
    public void setAttack(int val) throws InvalidArgumentException {
        if(val < 1 || val > 1000) {
            throw new InvalidArgumentException("The attack value cannot be less than 1 or greater than 1000.", "Il valore " +
                    "di attacco non puo' essere minore di 1 o maggiore di 1000.");
        }

        attack = val;
    }

    /**
     * Sets the release value in milliseconds.
     * @param val The given release value in milliseconds
     * @throws InvalidArgumentException If the given release value is less than 5 or greater than 2000
     */
    public void setRelease(int val) throws InvalidArgumentException {
        if(val < 5 || val > 2000) {
            throw new InvalidArgumentException("The release value cannot be less than 5 or greater than 2000.", "Il valore " +
                    "di rilascio non puo' essere minore di 5 o maggiore di 2000.");
        }

        release = val;
    }

    /**
     * Adds a new channel ID to this filter.
     * @param channelID The channel ID to be added
     * @throws InvalidArgumentException If the given channel ID is null, an empty string or any value not recognized by FFmpeg
     */
    public void addChannel(@NotNull String channelID) throws InvalidArgumentException {
        chLayout.addChannelID(channelID);
    }

    //To be tested
    @Override
    protected void updateMap() {
        options.put("transfer", transfer);
        options.put("attack", String.valueOf(attack));
        options.put("release", String.valueOf(release));
        options.put("channels", (chLayout.getChannels().isEmpty()) ? "all" : chLayout.toString());
    }
}
