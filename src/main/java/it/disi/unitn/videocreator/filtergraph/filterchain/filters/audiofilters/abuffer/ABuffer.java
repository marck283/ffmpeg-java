package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.abuffer;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.aformat.ChannelLayout;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements FFmpeg's "abuffer" filter.
 */
public class ABuffer extends AudioFilter {

    private String time_base, sample_fmt;

    private int sample_rate, channels;

    private ChannelLayout channel_layout;

    /**
     * This class's constructor.
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public ABuffer() throws InvalidArgumentException {
        super("abuffer");
        channels = -1;
        channel_layout = new ChannelLayout();
    }

    /**
     * Sets this field's time_base parameter.
     * @param time_base The given time_base value. This value cannot be null or an empty string
     * @throws InvalidArgumentException If the given value is null or an empty string
     */
    public void setTimeBase(@NotNull String time_base) throws InvalidArgumentException {
        if(time_base == null || time_base.isEmpty()) {
            throw new InvalidArgumentException("The time_base parameter cannot be null or an empty string.", "Il valore " +
                    "del parametro \"time_base\" non puo' essere null o una stringa vuota.");
        }
        this.time_base = time_base;
    }

    /**
     * Sets this field's sample_rate parameter.
     * @param sample_rate The given sample_rate value.
     */
    public void setSampleRate(int sample_rate) {
        this.sample_rate = sample_rate;
    }

    /**
     * Sets this field's sample_fmt parameter.
     * @param sample_fmt The given sample_fmt value. This value cannot be null or an empty string
     * @throws InvalidArgumentException If the given value is null or an empty string
     */
    public void setSampleFmt(@NotNull String sample_fmt) throws InvalidArgumentException {
        if(sample_fmt == null || sample_fmt.isEmpty()) {
            throw new InvalidArgumentException("The sample_fmt value cannot be null or an empty string.", "Il valore del " +
                    "parametro \"sample_fmt\" non puo' essere null o una stringa vuota.");
        }
        this.sample_fmt = sample_fmt;
    }

    /**
     * Sets this field's channel_layout parameter.
     * @param channel_layout The given ChannelLayout instance. This parameter cannot be null
     * @throws InvalidArgumentException If the given value is null
     */
    public void setChannelLayout(@NotNull ChannelLayout channel_layout) throws InvalidArgumentException {
        if(channel_layout == null) {
            throw new InvalidArgumentException("The given ChannelLayout instance cannot be null.", "L'istanza di ChannelLayout " +
                    "fornita non puo' essere null.");
        }
        this.channel_layout = channel_layout;
    }

    /**
     * Sets the number of channels.
     * @param channels The given number of channels
     */
    public void setChannels(int channels) {
        this.channels = channels;
    }

    @Override
    public void updateMap() {
        options.put("time_base", time_base);
        options.put("sample_fmt", sample_fmt);
        options.put("sample_rate", String.valueOf(sample_rate));
        options.put("channel_layout", channel_layout.toString());
        if(channels != -1) {
            options.put("channels", String.valueOf(channels));
        }
    }
}
