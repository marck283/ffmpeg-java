package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.aformat;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.MultiLanguageUnsupportedOperationException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class acts as a wrapper around the "aformat" FFmpeg filter.
 */
public class AFormat extends AudioFilter {

    private final List<String> sample_fmts, sample_rates;

    //private final List<ChannelLayout> channel_layouts;

    private final ChannelLayout chLayout;

    /**
     * This class's constructor.
     *
     */
    public AFormat() {
        super("aformat");
        sample_fmts = new ArrayList<>();
        sample_rates = new ArrayList<>();
        //channel_layouts = new ArrayList<>();
        chLayout = new ChannelLayout();
    }

    /**
     * Adds the given sample format to the filter. ATTENTION: in order to use this filter, the user must make sure that the
     * given audio format is supported by FFmpeg.
     *
     * @param sample_format The given sample format
     * @throws InvalidArgumentException If the given sample format is null or an empty string
     */
    public void addSampleFormat(@NotNull String sample_format) throws InvalidArgumentException {
        if (StringExt.checkNullOrEmpty(sample_format)) {
            throw new InvalidArgumentException("The given sample format cannot be null or an empty string.", "Il formato " +
                    "audio fornito non puo' essere null o una stringa vuota.");
        }

        sample_fmts.add(sample_format);
    }

    /**
     * Adds the gien sample rate to this filter. ATTENTION: in order to use this filter, the user must make sure that the
     * given sample rate is supported by FFMpeg.
     *
     * @param sample_rate The given sample rate
     * @throws InvalidArgumentException If the given sample rate is null or an empty string
     */
    public void addSampleRate(@NotNull String sample_rate) throws InvalidArgumentException {
        if (StringExt.checkNullOrEmpty(sample_rate)) {
            throw new InvalidArgumentException("The given sample rate cannot be null or an empty string.", "La data frequenza " +
                    "audio non puo' essere null o una stringa vuota.");
        }

        sample_rates.add(sample_rate);
    }

    /**
     * Adds a new channel layout to this filter.
     *
     * @param chid The given channel ID
     * @throws InvalidArgumentException If the given channel ID is null or an empty string
     * @throws MultiLanguageUnsupportedOperationException If the user already specified a combination of audio channels
     * by calling setChannelID()
     */
    public void addChannelLayout(@NotNull String chid) throws InvalidArgumentException, MultiLanguageUnsupportedOperationException {
        chLayout.addChannelID(chid);
    }

    /**
     * Adds the given ChannelLayout instance to this filter.
     *
     * @param chlay The given ChannelLayout instance
     * @throws InvalidArgumentException If the given ChannelLayout instance is null
     */
    public void addChannelLayout(@NotNull ChannelLayout chlay) throws InvalidArgumentException {
        chLayout.addChannels(chlay);
    }

    @Override
    public void updateMap() {
        options.put("sample_fmts", String.join("|", sample_fmts));
        options.put("sample_rates", String.join("|", sample_rates));
        options.put("channel_layouts", chLayout.toStringBar());
    }
}
