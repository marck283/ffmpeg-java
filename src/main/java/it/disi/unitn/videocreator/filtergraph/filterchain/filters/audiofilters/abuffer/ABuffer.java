package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.abuffer;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.aformat.ChannelLayout;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements FFmpeg's "abuffer" filter. This filter must be used in conjunction with a ffmpeg command trying
 * to process a video.
 */
public class ABuffer extends AudioFilter {

    private float time_base;

    private String sample_fmt;

    private int sample_rate, channels;

    private ChannelLayout channel_layout;

    /**
     * This class's constructor.
     *
     */
    public ABuffer() {
        super("abuffer");
        channels = 1;
        channel_layout = new ChannelLayout();
        time_base = 0F;
        sample_rate = 0;
        sample_fmt = "";
    }

    /**
     * Sets this filter's time_base parameter.
     *
     * @param time_base The given time_base value. This value cannot be null or an empty string, and it has to be in the
     *                  form num/den.
     * @throws InvalidArgumentException If the given value is null or an empty string
     */
    public void setTimeBase(@NotNull String time_base) throws InvalidArgumentException {
        if (time_base == null || time_base.isEmpty()) {
            throw new InvalidArgumentException("The time_base parameter cannot be null or an empty string.", "Il valore " +
                    "del parametro \"time_base\" non puo' essere null o una stringa vuota.");
        }
        if (!time_base.contains("/") || time_base.charAt(0) == '/' || time_base.charAt(time_base.length() - 1) == '/') {
            throw new InvalidArgumentException("The time_base value given to this method must be in the form <num1>/<num2>.",
                    "Il valore del parametro time_base fornito a questo metodo deve essere della forma <num1>/<num2>.");
        }

        String[] timeBaseArr = time_base.split("/");
        setTimeBase(Float.parseFloat(timeBaseArr[0]), Float.parseFloat(timeBaseArr[1]));
    }

    /**
     * This method sets the time_base parameter's value as a floating-point number given a fraction's numerator and
     * denominator.
     *
     * @param num The numerator.
     * @param den The denominator.
     */
    public void setTimeBase(float num, float den) {
        time_base = num / den;
    }

    /**
     * Sets this field's sample_rate parameter.
     *
     * @param sample_rate The given sample_rate value.
     */
    public void setSampleRate(int sample_rate) {
        this.sample_rate = sample_rate;
    }

    /**
     * Sets this field's sample_fmt parameter.
     *
     * @param sample_fmt The given sample_fmt value. This value cannot be null or an empty string
     * @throws InvalidArgumentException If the given value is null or an empty string
     */
    public void setSampleFmt(@NotNull String sample_fmt) throws InvalidArgumentException {
        if (sample_fmt == null || sample_fmt.isEmpty()) {
            throw new InvalidArgumentException("The sample_fmt value cannot be null or an empty string.", "Il valore del " +
                    "parametro \"sample_fmt\" non puo' essere null o una stringa vuota.");
        }
        this.sample_fmt = sample_fmt;
    }

    /**
     * Sets this field's channel_layout parameter.
     *
     * @param channel_layout The given ChannelLayout instance. This parameter cannot be null
     * @throws InvalidArgumentException If the given value is null
     */
    public void setChannelLayout(@NotNull ChannelLayout channel_layout) throws InvalidArgumentException {
        if (channel_layout == null) {
            throw new InvalidArgumentException("The given ChannelLayout instance cannot be null.", "L'istanza di ChannelLayout " +
                    "fornita non puo' essere null.");
        }
        this.channel_layout = channel_layout;
    }

    /**
     * Sets the number of channels.
     *
     * @param channels The given number of channels. This value must be greater than or equal to 1.
     * @throws InvalidArgumentException If the given number of channels is less than or equal to zero
     */
    public void setChannels(int channels) throws InvalidArgumentException {
        if (channels <= 0) {
            throw new InvalidArgumentException("Cannot set number of channels less than or equal to zero.", "Non e' " +
                    "possibile impostare un numero di canali minore o uguale a zero.");
        }
        this.channels = channels;
    }

    @Override
    public void updateMap() {
        options.put("time_base", String.valueOf(time_base));
        options.put("sample_fmt", sample_fmt);
        options.put("sample_rate", String.valueOf(sample_rate));
        options.put("channel_layout", channel_layout.toString());
        options.put("channels", String.valueOf(channels));
    }
}
