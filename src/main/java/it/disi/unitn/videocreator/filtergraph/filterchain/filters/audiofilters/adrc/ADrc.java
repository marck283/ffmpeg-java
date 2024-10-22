package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.adrc;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.MultiLanguageUnsupportedOperationException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.aformat.ChannelLayout;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements FFmpeg's "adrc" filter. ATTENTION: this class is still being tested.
 */
public class ADrc extends AudioFilter {

    enum Transfer {
        CH, SN, NB_CHANNELS, T, SR, P, F;

        @Override
        public @NotNull String toString() {
            switch (this) {
                case CH -> {
                    return "ch";
                }
                case SN -> {
                    return "sn";
                }
                case NB_CHANNELS -> {
                    return "nb_channels";
                }
                case T -> {
                    return "t";
                }
                case SR -> {
                    return "sr";
                }
                case F -> {
                    return "f";
                }
                default -> {
                    return "p";
                }
            }
        }
    }

    private Transfer transfer;

    private int attack, release;

    private final ChannelLayout chLayout;

    /**
     * This class's constructor.
     *
     */
    public ADrc() {
        super("adrc");
        chLayout = new ChannelLayout();
        attack = 50;
        release = 100;
        transfer = Transfer.P;
    }

    /**
     * Sets the transfer expression. This value must be recognizable by FFmpeg.
     *
     * @param val The given transfer expression
     * @throws InvalidArgumentException If the given transfer expression is null or an empty string or not a value recognized
     *                                  by FFmpeg
     */
    public void setTransfer(@NotNull String val) throws InvalidArgumentException {
        if (StringExt.checkNullOrEmpty(val)) {
            throw new InvalidArgumentException("The transfer expression cannot be null or an empty string.", "L'espressione " +
                    "di trasferimento non puo' essere null o una stringa vuota.");
        }

        switch (val) {
            case "ch" -> transfer = Transfer.CH;
            case "sn" -> transfer = Transfer.SN;
            case "nb_channels" -> transfer = Transfer.NB_CHANNELS;
            case "t" -> transfer = Transfer.T;
            case "sr" -> transfer = Transfer.SR;
            case "f" -> transfer = Transfer.F;
            case "p" -> transfer = Transfer.P;
            default -> throw new InvalidArgumentException("The transfer expression must be equal to \"ch\", \"sn\", " +
                    "\"nb_channels\", \"t\", \"sr\", \"f\" or \"p\".", "L'espressione di trasferimento deve essere " +
                    "uguale a \"ch\", \"sn\", \"nb_channels\", \"t\", \"sr\", \"f\" o \"p\".");
        }
    }

    /**
     * Sets the attack value in milliseconds.
     *
     * @param val The given attack value in milliseconds
     * @throws InvalidArgumentException If the given attack value is less than 1 or greater than 1000
     */
    public void setAttack(int val) throws InvalidArgumentException {
        if (val < 1 || val > 1000) {
            throw new InvalidArgumentException("The attack value cannot be less than 1 or greater than 1000.", "Il valore " +
                    "di attacco non puo' essere minore di 1 o maggiore di 1000.");
        }

        attack = val;
    }

    /**
     * Sets the release value in milliseconds.
     *
     * @param val The given release value in milliseconds
     * @throws InvalidArgumentException If the given release value is less than 5 or greater than 2000
     */
    public void setRelease(int val) throws InvalidArgumentException {
        if (val < 5 || val > 2000) {
            throw new InvalidArgumentException("The release value cannot be less than 5 or greater than 2000.", "Il valore " +
                    "di rilascio non puo' essere minore di 5 o maggiore di 2000.");
        }

        release = val;
    }

    /**
     * Adds a new channel ID to this filter.
     *
     * @param channelID The channel ID to be added
     * @throws InvalidArgumentException      If the given channel ID is null, an empty string or any value not recognized by
     *                                       FFmpeg
     * @throws MultiLanguageUnsupportedOperationException If the channel identified by the given channel ID cannot be added
     */
    public void addChannel(@NotNull String channelID) throws InvalidArgumentException, MultiLanguageUnsupportedOperationException {
        chLayout.addChannelID(channelID);
    }

    //To be tested
    @Override
    public void updateMap() {
        options.put("transfer", transfer.toString());
        options.put("attack", String.valueOf(attack));
        options.put("release", String.valueOf(release));
        options.put("channels", (chLayout.isEmpty()) ? "all" : chLayout.toString());
    }
}
