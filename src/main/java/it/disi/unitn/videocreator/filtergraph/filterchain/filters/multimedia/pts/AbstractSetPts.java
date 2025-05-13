package it.disi.unitn.videocreator.filtergraph.filterchain.filters.multimedia.pts;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSetPts extends Filter {

    /**
     * The timestamps' expression. This value defaults to "1*PTS".
     */
    protected String expr;

    private final String filterName;

    /**
     * This class's constructor.
     *
     * @param filterName The filter's name
     */
    protected AbstractSetPts(@NotNull String filterName) {
        super(filterName);

        this.filterName = filterName;
        expr = "1*PTS";
    }

    protected boolean checkExpr(@NotNull String expr) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(expr)) {
            throw new InvalidArgumentException("The given expression for the " + filterName + " filter cannot be null or " +
                    "an empty string.", "L'espressione fornita per il filtro " + filterName + " non puo' essere null o " +
                    "una stringa vuota.");
        }

        return expr.contains("SAMPLE_RATE") || expr.contains("SR") || expr.contains("STARTPTS") || expr.contains("STARTT")
                || expr.contains("INTERLACED") || expr.contains("T") || expr.contains("POS") || expr.contains("PREV_INPTS")
                || expr.contains("PREV_INT") || expr.contains("PREV_OUTPTS") || expr.contains("PREV_OUTT") ||
                expr.contains("RTCTIME") || expr.contains("RTCSTART") || expr.contains("TB") || expr.contains("T_CHANGE");
    }

    @Override
    public void updateMap() {
        options.put("expr", expr);
    }
}
