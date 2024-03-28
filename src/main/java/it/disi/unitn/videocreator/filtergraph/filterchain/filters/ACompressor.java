package it.disi.unitn.videocreator.filtergraph.filterchain.filters;

import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

/**
 * Wrapper class for the acompressor FFmpeg filter.
 */
public class ACompressor extends Filter {

    /**
     * The class's constructor.
     */
    protected ACompressor() {
        super("acompressor");
    }

    /**
     * Sets the input gain.
     * @param val The new input gain
     * @throws InvalidArgumentException If the given input gain is less than 0.015625 or greater than 64
     */
    public void setLevelIn(double val) throws InvalidArgumentException {
        if(val < 0.015625 || val > 64.0) {
            throw new InvalidArgumentException("The input gain cannot be less than 0.015625 or greater than 64.",
                    "L'input gain non puo' essere inferiore a 0.015625 o maggiore di 64.");
        }
        String lvlin = String.valueOf(val);
        options.put("level_in", lvlin);
    }

    private boolean checkNullOrEmpty(@NotNull String val) {
        return val == null || val.isEmpty();
    }

    /**
     * Sets the mode.
     * @param val The new mode
     * @throws InvalidArgumentException If the new mode is null, empty or a value different from "downward" or "upward"
     */
    public void setMode(@NotNull String val) throws InvalidArgumentException {
        if(checkNullOrEmpty(val) || !(val.equals("downward") || val.equals("upward"))) {
            throw new InvalidArgumentException("The mode given to the ACompressor filter cannot be null, empty or a value " +
                    "different from \"downward\" and \"upward\".", "La modalita' fornita al filtro ACompressor non puo' " +
                    "essere null, vuota o corrispondere ad un valore diverso da \"downward\" o \"upward\".");
        }
        options.put("mode", val);
    }

    /**
     * Sets the threshold.
     * @param val The new threshold
     * @throws InvalidArgumentException If the new threshold is less than 0.00097563 or greater than 1
     */
    public void setThreshold(double val) throws InvalidArgumentException {
        if(val < 0.00097563 || val > 1) {
            throw new InvalidArgumentException("The threshold given to the ACompressor filter cannot be less than " +
                    "0.00097563 or greater than 1.", "Il limite massimo fornito al filtero ACompressor non puo' essere " +
                    "inferiore a 0.00097563 o maggiore di 1.");
        }
        options.put("threshold", String.valueOf(val));
    }

    /**
     * Sets the ratio.
     * @param val The new ratio
     * @throws InvalidArgumentException If the given ratio is less than 1 or greater than 20
     */
    public void setRatio(int val) throws InvalidArgumentException {
        //Remember that it is possible to have a ratio of 1:2!
        if(val < 1 || val > 20) {
            throw new InvalidArgumentException("The ratio given to the ACompressor filter cannot be less than 1 or " +
                    "greater than 20.", "La proporzione data al filtro ACompressor non puo' essere inferiore a 1 o " +
                    "maggiore di 20.");
        }
        options.put("ratio", String.valueOf(val));
    }

    /**
     * Sets the amount of milliseconds the signal has to rise above the threshold before gain reduction starts.
     * @param val The amount of milliseconds the signal has to rise above the threshold before gain reduction starts
     * @throws InvalidArgumentException If the given amount of milliseconds the signal has to rise above the threshold before
     * gain reduction starts is less than 0.01 or greater than 2000
     */
    public void setAttack(double val) throws InvalidArgumentException {
        if(val < 0.01 || val > 2000) {
            throw new InvalidArgumentException("The amount of milliseconds the signal has to rise above the threshold " +
                    "before gain reduction starts cannot be less than 0.01 or greater than 2000.", "Il numero di " +
                    "millisecondi per i quali il segnale deve salire sopra il limite massimo prima che parta la fase di " +
                    "gain reduction non puo' essere minore di 0.01 o maggiore di 2000.");
        }
        options.put("attack", String.valueOf(val));
    }

    /**
     * Sets the amount of milliseconds the signal has to fall below the threshold before reduction is decreased again.
     * @param val The amount of milliseconds the signal has to fall below the threshold before reduction is decreased again
     * @throws InvalidArgumentException If the given amount of milliseconds the signal has to fall below the threshold before
     * reduction is decreased again is less than 0.01 or greater than 9000
     */
    public void setRelease(double val) throws InvalidArgumentException {
        if(val < 0.01 || val > 9000) {
            throw new InvalidArgumentException("The amount of milliseconds the signal has to fall below the threshold " +
                    "before reduction is decreased again cannot be less than 0.01 or greater than 9000.", "Il numero di " +
                    "millisecondi per i quali il segnale deve scendere sotto il limite massimo prima che la riduzione " +
                    "diminuisca nuovamente non puo' essere minore di 0.01 o maggiore di 9000.");
        }
        options.put("release", String.valueOf(val));
    }

    /**
     * Sets the makeup value.
     * @param val The new makeup value
     * @throws InvalidArgumentException If the new makeup value is less than 1 or greater than 64
     */
    public void setMakeup(int val) throws InvalidArgumentException {
        if(val < 1 || val > 64) {
            throw new InvalidArgumentException("The makeup value given to the ACompressor filter cannot be less than 1 " +
                    "or greater than 64.", "Il valore di amplificazione del segnale in post-processing non puo' essere " +
                    "inferiore a 1 o maggiore di 64.");
        }
        options.put("makeup", String.valueOf(val));
    }

    /**
     * Sets the knee value.
     * @param val The new knee value. Must be between 1.0 and 8.0
     * @throws InvalidArgumentException If the new knee value is less than 1.0 or greater than 8.0
     */
    public void setKnee(double val) throws InvalidArgumentException {
        if(val < 1.0 || val > 8.0) {
            throw new InvalidArgumentException("The \"knee\" value cannot be less than 1 or greater than 8.", "Il valore " +
                    "del parametro \"knee\" non puo' essere inferiore a 1 o maggiore di 8.");
        }
        options.put("knee", String.valueOf(val));
    }

    /**
     * Sets the link value.
     * @param val The new link value
     * @throws InvalidArgumentException If the new link value is null, empty or different from "average" or "maximum"
     */
    public void setLink(@NotNull String val) throws InvalidArgumentException {
        if(checkNullOrEmpty(val) || !(val.equals("average") || val.equals("maximum"))) {
            throw new InvalidArgumentException("The link value cannot be null, empty or different from \"average\" or " +
                    "\"maximum\".", "Il valore del parametro \"link\" non puo' essere null, vuoto o diverso da \"average\" " +
                    "o \"maximum\".");
        }
        options.put("link", val);
    }

    /**
     * Sets the detection value.
     * @param val The new detection value
     * @throws InvalidArgumentException If the new detection value is null, empty or different from "peak" or "rms"
     */
    public void setDetection(@NotNull String val) throws InvalidArgumentException {
        if(checkNullOrEmpty(val) || !(val.equals("peak") || val.equals("rms"))) {
            throw new InvalidArgumentException("The detection value cannot be null, empty or different from \"peak\" or " +
                    "\"rms\".", "il valore del parametro \"detection\" non puo' essere null, vuoto o diverso da \"peak\" " +
                    "o \"rms\"");
        }
        options.put("detection", val);
    }

    /**
     * Sets the new mix value.
     * @param val The new mix value
     * @throws InvalidArgumentException If the new mix value is less than 0.0 or greater than 1.0
     */
    public void setMix(double val) throws InvalidArgumentException {
        if(val < 0.0 || val > 1.0) {
            throw new InvalidArgumentException("The \"mix\" value cannot be less than 0.0 or greater than 1.0.", "Il " +
                    "valore del parametro \"mix\" non puo' essere inferiore a 0.0 o maggiore di 1.0.");
        }
        options.put("mix", String.valueOf(val));
    }
}
