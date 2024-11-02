package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.geq;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;
import org.jetbrains.annotations.NotNull;

/**
 * This class sets the parameters for ffmpeg's "geq" filter.
 */
public class Geq extends VideoFilter {

    private String lum, cb, cr, a, r, g, b;

    /**
     * This class's constructor. Constructs a new video filter.
     *
     */
    public Geq() {
        super("geq");
    }

    private void setParam(@NotNull String optName, @NotNull String optVal) {
        if(StringExt.checkNullOrEmpty(optVal)) {
            System.err.println((new InvalidArgumentException("The \"" + optName + "\" expression cannot be null or an " +
                    "empty string.", "L'espressione per il parametro \"" + optName + "\" non puo' essere null o una " +
                    "stringa vuota.")).getMessage());
        }

        switch(optName) {
            case "lum" -> this.lum = optVal;
            case "cb" -> this.cb = optVal;
            case "cr" -> this.cr = optVal;
            case "a" -> this.a = optVal;
            case "r" -> this.r = optVal;
            case "g" -> this.g = optVal;
            case "b" -> this.b = optVal;
            default -> {
                System.err.println((new InvalidArgumentException("Invalid option name.", "Nome opzione non valido."))
                        .getMessage());
                System.exit(6);
            }
        }
    }

    /**
     * This method sets the "lum" parameter.
     * @param lum The given "lum" expression. This value cannot be null or an empty string.
     */
    public void setLum(@NotNull String lum) {
        setParam("lum", lum);
    }

    /**
     * This method sets the "cb" parameter.
     * @param cb The given "cb" expression. This value cannot be null or an empty string.
     */
    public void setCB(@NotNull String cb) {
        setParam("cb", cb);
    }

    /**
     * This method sets the "cr" parameter.
     * @param cr The given "cr" expression. This value cannot be null or an empty string.
     */
    public void setCR(@NotNull String cr) {
        setParam("cr", cr);
    }

    /**
     * This method sets the "a" parameter.
     * @param a The given "a" expression. This value cannot be null or an empty string.
     */
    public void setA(@NotNull String a) {
        setParam("a", a);
    }

    /**
     * This method sets the "r" parameter.
     * @param r The given "r" expression. This value cannot be null or an empty string.
     */
    public void setR(@NotNull String r) {
        setParam("r", r);
    }

    /**
     * This method sets the "g" parameter.
     * @param g The given "g" expression. This value cannot be null or an empty string.
     */
    public void setG(@NotNull String g) {
        setParam("g", g);
    }

    /**
     * This method sets the "b" parameter.
     * @param b The given "b" expression. This value cannot be null or an empty string.
     */
    public void setB(@NotNull String b) {
        setParam("b", b);
    }

    private void setOptionWithNullEmptyCheck(@NotNull String oname, @NotNull String val) throws InvalidArgumentException {
        super.setOptionWithCond(!StringExt.checkNullOrEmpty(val), oname, val);
    }

    @Override
    public void updateMap() {
        try {
            setOptionWithNullEmptyCheck("lum", lum);
            setOptionWithNullEmptyCheck("cb", cb);
            setOptionWithNullEmptyCheck("cr", cr);
            setOptionWithNullEmptyCheck("a", a);
            setOptionWithNullEmptyCheck("r", r);
            setOptionWithNullEmptyCheck("g", g);
            setOptionWithNullEmptyCheck("b", b);
        } catch(InvalidArgumentException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
