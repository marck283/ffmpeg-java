package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.acrossfade;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements curve types used in the "acrossfade" and "afade" audio filters.
 */
public class Curve {

    private String curve;

    public Curve() {
        curve = "";
    }

    /**
     * This method sets the curve value.
     * @param curve The given curve value. This value must not be null or empty, and it must be accepted by FFmpeg.
     *              See the documentation for further details.
     * @throws InvalidArgumentException If the given value is null, an empty string, or it is not recognized by FFmpeg
     */
    public void setCurve(@NotNull String curve) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(curve)) {
            throw new InvalidArgumentException("The curve given to this method cannot be null or an empty string.",
                    "La curva fornita a questo metodo non puo' essere null o una stringa vuota.");
        }

        switch(curve) {
            case "tri", "qsin", "hsin", "esin", "log", "ipar", "qua", "cub", "squ", "cbr", "par", "exp", "iqsin", "ihsin",
                 "dese", "desi", "losi", "sinc", "isinc", "quat", "quatr", "qsin2", "hsin2", "nofade" -> this.curve = curve;
            default -> throw new InvalidArgumentException("The curve given to this method must be accepted by FFmpeg.",
                    "La curva fornita a questo metodo deve essere accettata da FFmpeg.");
        }
    }

    public String toString() {
        if(!StringExt.checkNullOrEmpty(curve)) {
            return curve;
        }
        return null;
    }

}
