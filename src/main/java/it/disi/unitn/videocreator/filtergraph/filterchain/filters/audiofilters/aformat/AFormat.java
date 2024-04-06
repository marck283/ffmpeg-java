package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.aformat;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class acts as a wrapper around the "aformat" FFmpeg filter.
 */
public class AFormat extends AudioFilter {

    private final List<String> sample_fmts;

    private final List<String> sample_rates;

    private final List<ChannelLayout> channel_layouts;

    /**
     * This class's constructor.
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public AFormat() throws InvalidArgumentException {
        super("aformat");
        sample_fmts = new ArrayList<>();
        sample_rates = new ArrayList<>();
        channel_layouts = new ArrayList<>();
    }

    /**
     * Adds the given sample format to the filter. ATTENTION: in order to use this filter, the user must make sure that the
     * given audio format is supported by FFmpeg.
     * @param sample_format The given sample format
     * @throws InvalidArgumentException If the given sample format is null or an empty string
     */
    public void addSampleFormat(@NotNull String sample_format) throws InvalidArgumentException {
        if(sample_format == null || sample_format.isEmpty()) {
            throw new InvalidArgumentException("The given sample format cannot be null or an empty string.", "Il formato " +
                    "audio fornito non puo' essere null o una stringa vuota.");
        }

        sample_fmts.add(sample_format);
    }

    /**
     * Adds the gien sample rate to this filter. ATTENTION: in order to use this filter, the user must make sure that the
     * given sample rate is supported by FFMpeg.
     * @param sample_rate The given sample rate
     * @throws InvalidArgumentException If the given sample rate is null or an empty string
     */
    public void addSampleRate(@NotNull String sample_rate) throws InvalidArgumentException {
        if(sample_rate == null || sample_rate.isEmpty()) {
            throw new InvalidArgumentException("The given sample rate cannot be null or an empty string.", "La data frequenza " +
                    "audio non puo' essere null o una stringa vuota.");
        }

        sample_rates.add(sample_rate);
    }

    /**
     * Adds a new ChannelLayout instance to this filter.
     * @return The newly created ChannelLayout instance
     */
    public ChannelLayout addChannelLayout() {
        ChannelLayout chlay = new ChannelLayout();
        channel_layouts.add(chlay);
        return chlay;
    }

    @Override
    public void updateMap() {
        options.put("sample_fmts", String.join("|", sample_fmts));
        options.put("sample_rates", String.join("|", sample_rates));

        List<String> chLayouts = new ArrayList<>();
        for(ChannelLayout cl: channel_layouts) {
            chLayouts.add(cl.toString());
        }
        options.put("channel_layouts", String.join("|", chLayouts));
    }
}
