package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.adynamicequalizer;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements FFmpeg's "adynamicequalizer" filter.
 */
public class ADynamicEqualizer extends AudioFilter {

    private int threshold = 0, dfrequency = 1000, tfrequency = 1000, attack = 20, release = 200, ratio = 1, makeup = 0,
    range = 50;

    private double dqfactor = 1.0, tqfactor = 1.0;

    private String mode = "cutbelow", dftype = "bandpass", tftype = "bell", auto = "disabled", precision = "auto";

    /**
     * This class's constructor.
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public ADynamicEqualizer() throws InvalidArgumentException {
        super("adynamicequalizer");
    }

    /**
     * Sets the threshold value.
     * @param val The new threshold value
     * @throws InvalidArgumentException If the new threshold value is less than 0 or greater than 100
     */
    public void setThreshold(int val) throws InvalidArgumentException {
        if(val < 0 || val > 100) {
            throw new InvalidArgumentException("Invalid threshold value.", "Valore threshold non valido.");
        }

        threshold = val;
    }

    /**
     * Sets the dfrequency value.
     * @param val The new dfrequency value
     * @throws InvalidArgumentException If the new dfrequency value is less than 2 or greater than 1000000
     */
    public void setDFrequency(int val) throws InvalidArgumentException {
        if(val < 2 || val > 1000000) {
            throw new InvalidArgumentException("Invalid dfrequency value.", "Valore dfrequency non valido.");
        }

        dfrequency = val;
    }

    /**
     * Sets the dqfactor value.
     * @param val The new dqfactor value
     * @throws InvalidArgumentException If the new dqfactor value is less than 0.001 or greater than 1000.0
     */
    public void setDQFactor(double val) throws InvalidArgumentException {
        if(val < 0.001 || val > 1000.0) {
            throw new InvalidArgumentException("Invalid dqfactor value.", "Valore dqfactor non valido.");
        }

        dqfactor = val;
    }

    /**
     * Sets the tfrequency value.
     * @param val The new tfrequency value
     * @throws InvalidArgumentException If the new tfrequency value is less than 2 or greater than 1000000
     */
    public void setTfrequency(int val) throws InvalidArgumentException {
        if(val < 2 || val > 1000000) {
            throw new InvalidArgumentException("Invalid tfrequency value.", "Valore tfrequency non valido.");
        }

        tfrequency = val;
    }

    /**
     * Sets the tqfactor value.
     * @param val The new tqfactor value
     * @throws InvalidArgumentException If the new tqfactor value is less than 0.001 or greater than 1000.0
     */
    public void setTQFactor(double val) throws InvalidArgumentException {
        if(val < 0.001 || val > 1000.0) {
            throw new InvalidArgumentException("Invalid tqfactor value.", "Valore tqfactor non valido.");
        }

        tqfactor = val;
    }

    /**
     * Sets the new attack value.
     * @param val The new attack value
     * @throws InvalidArgumentException If the new attack value is less than 1 or greater than 2000
     */
    public void setAttack(int val) throws InvalidArgumentException {
        if(val < 1 || val > 2000) {
            throw new InvalidArgumentException("Invalid attack value.", "Valore attack non valido.");
        }

        attack = val;
    }

    /**
     * Sets the release value.
     * @param val The new release value
     * @throws InvalidArgumentException If the enw release value is less than 1 or greater than 2000
     */
    public void setRelease(int val) throws InvalidArgumentException {
        if(val < 1 || val > 2000) {
            throw new InvalidArgumentException("Invalid release value.", "Valore release non valido.");
        }

        release = val;
    }

    /**
     * Sets the ratio value.
     * @param val The new ratio value
     * @throws InvalidArgumentException If the new ratio value is less than 0 or greater than 30
     */
    public void setRatio(int val) throws InvalidArgumentException {
        if(val < 0 || val > 30) {
            throw new InvalidArgumentException("Invalid ratio value.", "Valore ratio non valido.");
        }

        ratio = val;
    }

    /**
     * Sets the makeup value.
     * @param val The new makeup value
     * @throws InvalidArgumentException If the new makeup value is less than 0 or greater than 100
     */
    public void setMakeup(int val) throws InvalidArgumentException {
        if(val < 0 || val > 100) {
            throw new InvalidArgumentException("Invalid makeup value.", "Valore makeup non valido.");
        }

        makeup = val;
    }

    /**
     * Sets the range value.
     * @param val The new range value
     * @throws InvalidArgumentException If the new range value is less than 1 or greater than 200
     */
    public void setRange(int val) throws InvalidArgumentException {
        if(val < 1 || val > 200) {
            throw new InvalidArgumentException("Invalid range value.", "Valore range non valido.");
        }

        range = val;
    }

    /**
     * Sets the mode of filter operation.
     * @param mode The mode of filter operation
     * @throws InvalidArgumentException If the mode of filter operation is null, empty or different from "listen", "cutbelow",
     * "cutabove", "boostbelow" or "boostabove"
     */
    public void setMode(@NotNull String mode) throws InvalidArgumentException {
        if(checkNullOrEmpty(mode)) {
            throw new InvalidArgumentException("The mode of filter operation cannot be null or an empty string.", "La " +
                    "modalita' con cui il filtro opera non puo' essere null o una stringa vuota.");
        }

        switch(mode) {
            case "listen", "cutbelow", "cutabove", "boostbelow", "boostabove" -> this.mode = mode;
            default -> throw new InvalidArgumentException("The mode of filter operation must be equal to \"listen\", " +
                    "\"cutbelow\", \"cutabove\", \"boostbelow\" or \"boostabove\".", "La modalita' con cui il filtro opera " +
                    "deve essere uguale a \"listen\", \"cutbelow\", \"cutabove\", \"boostbelow\" o \"boostabove\".");
        }
    }

    /**
     * Sets the detection filter's type.
     * @param dftype The new detection filter's type
     * @throws InvalidArgumentException If the given detection filter's type is null, empty or different from "bandpass",
     * "lowpass", "highpass" or "peak"
     */
    public void setDFType(@NotNull String dftype) throws InvalidArgumentException {
        if(checkNullOrEmpty(dftype)) {
            throw new InvalidArgumentException("The detection filter's type cannot be null or an empty string.", "Il tipo " +
                    "del filtro di rilevamento non puo' essere null o una stringa vuota.");
        }

        switch(dftype) {
            case "bandpass", "lowpass", "highpass", "peak" -> this.dftype = dftype;
            default -> throw new InvalidArgumentException("The detection filter's type must be equal to \"bandpass\", " +
                    "\"lowpass\", \"highpass\" or \"peak\".", "Il tipo del filtro di rilevamento deve essere uguale a " +
                    "\"bandpass\", \"lowpass\", \"highpass\" o \"peak\".");
        }
    }

    /**
     * Sets the target filter's type.
     * @param tftype The target filter's type
     * @throws InvalidArgumentException If the target filter's type is different from "bell", "lowshelf" or "highshelf".
     */
    public void setTFType(@NotNull String tftype) throws InvalidArgumentException {
        if(checkNullOrEmpty(tftype)) {
            throw new InvalidArgumentException("The target filter's type cannot be null or an empty string.", "Il tipo " +
                    "del filtro obiettivo non puo' essere null o una stringa vuota.");
        }

        switch(tftype) {
            case "bell", "lowshelf", "highshelf" -> this.tftype = tftype;
            default -> throw new InvalidArgumentException("The target filter's type must be equal to \"bell\", \"lowshelf\" " +
                    "or \"highshelf\".", "Il tipo del filtro obiettivo deve essere uguale a \"bell\", \"lowshelf\" o " +
                    "\"highshelf\".");
        }
    }

    /**
     * Sets the "auto" value.
     * @param auto The new "auto" value
     * @throws InvalidArgumentException If the given "auto" value is null, empty or different from "disabled", "off",
     * "on" or "adaptive"
     */
    public void setAuto(@NotNull String auto) throws InvalidArgumentException {
        if(checkNullOrEmpty(auto)) {
            throw new InvalidArgumentException("The \"auto\" value cannot be null or an empty string.", "Il valore \"auto\" " +
                    "non puo' essere null o una stringa vuota.");
        }

        switch(auto) {
            case "disabled", "off", "on", "adaptive" -> this.auto = auto;
            default -> throw new InvalidArgumentException("The \"auto\" value must be equal to \"disabled\", \"off\", " +
                    "\"on\" or \"adaptive\".", "Il valore \"auto\" deve essere uguale a \"disabled\", \"off\", \"on\" o " +
                    "\"adaptive\".");
        }
    }

    /**
     * Sets the precision value.
     * @param precision The new precision value
     * @throws InvalidArgumentException If the given precision value is different from "auto", "float" or "double"
     */
    public void setPrecision(@NotNull String precision) throws InvalidArgumentException {
        if(checkNullOrEmpty(precision)) {
            throw new InvalidArgumentException("The precision value cannot be null or an empty string.", "Il valore della " +
                    "precisione non puo' essere null o una stringa vuota.");
        }

        switch(precision) {
            case "auto", "float", "double" -> this.precision = precision;
            default -> throw new InvalidArgumentException("The precision value must be equal to \"auto\", \"float\" or " +
                    "\"double\".", "Il valore della precisione deve essere uguale a \"auto\", \"float\" o \"double\".");
        }
    }

    @Override
    public void updateMap() {
        options.put("threshold", String.valueOf(threshold));
        options.put("dfrequency", String.valueOf(dfrequency));
        options.put("dqfactor", String.valueOf(dqfactor));
        options.put("tfrequency", String.valueOf(tfrequency));
        options.put("tqfactor", String.valueOf(tqfactor));
        options.put("attack", String.valueOf(attack));
        options.put("release", String.valueOf(release));
        options.put("ratio", String.valueOf(ratio));
        options.put("makeup", String.valueOf(makeup));
        options.put("range", String.valueOf(range));
        options.put("mode", mode);
        options.put("dftype", dftype);
        options.put("tftype", tftype);
        options.put("auto", auto);
        options.put("precision", precision);
    }
}
