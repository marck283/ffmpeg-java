package it.disi.unitn.videocreator.filtergraph.filterchain.filters.multimedia.pts;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements the "setpts" multimedia filter.
 */
public class SetPts extends AbstractSetPts {

    /**
     * This class's constructor. Constructs a new filter (whether video or audio).
     */
    public SetPts() {
        super("setpts");
    }

    /**
     * This method sets the expression's value for the setpts filter.
     * @param expr The given expression's value. This value cannot be null or an empty string, and it must conform to
     *             FFmpeg's setpts filter specifications.
     * @throws InvalidArgumentException If the given value is null or an empty string, or if it does not conform to the
     * given specifications
     */
    public void setExpr(@NotNull String expr) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(expr)) {
            throw new InvalidArgumentException("The given expression for the setpts filter cannot be null or an empty " +
                    "string.", "L'espressione fornita per il filtro setpts non puo' essere null o una stringa vuota.");
        }

        if(!(super.checkExpr(expr) || expr.contains("FRAME_RATE") || expr.contains("FR") || expr.contains("PTS") || expr.contains("N"))) {
            throw new InvalidArgumentException("The given expression must conform to FFmpeg's specifications regarding " +
                    "the \"setpts\" filter.", "L'espressione fornita deve essere conforme alle specifiche di FFmpeg " +
                    "riguardo il filtro \"setpts\".");
        }

        this.expr = expr;
    }
}
