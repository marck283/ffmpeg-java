package it.disi.unitn.videocreator.filtergraph.filterchain.filters.multimedia.pts;

import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements the "asetpts" multimedia filter.
 */
public class ASetPts extends AbstractSetPts {

    /**
     * This class's constructor.
     */
    public ASetPts() {
        super("asetpts");
    }

    /**
     * This method sets the expression's value for the asetpts filter.
     * @param expr The given expression's value. This value cannot be null or an empty string, and it must conform to
     *             FFmpeg's asetpts filter specifications.
     * @throws InvalidArgumentException If the given value is null or an empty string
     */
    public void setExpr(@NotNull String expr) throws InvalidArgumentException {
        if(!super.checkExpr(expr) && !(expr.contains("FRAME_RATE") || expr.contains("FR") || expr.contains("PTS") ||
                expr.contains("N") || expr.contains("NB_CONSUMED_SAMPLES") || expr.contains("NB_SAMPLES") || expr.contains("S"))) {
            throw new InvalidArgumentException("The given expression must conform to FFmpeg's specifications regarding " +
                    "the \"asetpts\" filter.", "L'espressione fornita deve essere conforme alle specifiche di FFmpeg " +
                    "riguardo il filtro \"asetpts\".");
        }

        this.expr = expr;
    }


}
