package it.disi.unitn.videocreator.fps_mode;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements FFmpeg's "-fps_mode" option.
 */
public class FPSMode {

    private String stream_specifier, parameter;

    /**
     * The class's constructor.
     */
    public FPSMode() {
        stream_specifier = "";
        parameter = "";
    }

    /**
     * Sets the stream specifier.
     * @param stream_specifier The new stream specifier. Cannot be null or an empty string.
     * @throws InvalidArgumentException If the given stream specifier is null or an empty string
     */
    public void setStreamSpecifier(@NotNull String stream_specifier) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(stream_specifier)) {
            throw new InvalidArgumentException("The given stream specifier cannot be null or empty.", "L'identificativo " +
                    "dello stream fornito non puo' essere null o una stringa vuota.");
        }
        this.stream_specifier = stream_specifier;
    }

    /**
     * Sets the "parameter" value as required by the FFmpeg specification.
     * @param parameter The given "parameter" value. Cannot be null or an empty string or be different from "passthrough",
     *                  "cfr", "vfr" or auto.
     * @throws InvalidArgumentException If the given value is null, an empty string, or it is different from "passthrough",
     * "cfr", "vfr" or "auto"
     */
    public void setParameter(@NotNull String parameter) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(parameter)) {
            throw new InvalidArgumentException("The given \"parameter\" value cannot be null or an empty string.", "Il " +
                    "valore di \"parameter\" fornito non puo' essere null o una stringa vuota.");
        }
        switch(parameter) {
            case "passthrough", "0", "cfr", "1", "vfr", "2", "auto", "-1" -> this.parameter = parameter;
            default -> throw new InvalidArgumentException("The given \"parameter\" value cannot be different from " +
                    "\"passthrough\", \"cfr\", \"vfr\" or \"auto\" or their associated numerical values.", "Il valore di " +
                    "\"parameter\" fornito non puo' essere diverso da \"passthrough\", \"cfr\", \"vfr\" o \"auto\" o i " +
                    "loro valori numerici.");
        }
    }

    @Override
    public String toString() {
        if(stream_specifier != null && !stream_specifier.isEmpty()) {
            return "-fps_mode:" + stream_specifier + " " + parameter;
        }
        return "-fps_mode:" + parameter;
    }

}
