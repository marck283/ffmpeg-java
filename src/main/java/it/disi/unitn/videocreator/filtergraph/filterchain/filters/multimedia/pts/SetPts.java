package it.disi.unitn.videocreator.filtergraph.filterchain.filters.multimedia.pts;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements the "setpts" multimedia filter.
 */
public class SetPts extends VideoFilter {

    /**
     * The timestamps' expression. This value defaults to "1*PTS".
     */
    private String expr;

    /**
     * This class's constructor. Constructs a new filter (whether video or audio).
     */
    public SetPts() {
        super("setpts");

        expr = "1*PTS";
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

        if(!(expr.contains("FRAME_RATE") || expr.contains("FR") || expr.contains("PTS") || expr.contains("N") ||
                expr.contains("SAMPLE_RATE") || expr.contains("SR") || expr.contains("STARTPTS") || expr.contains("STARTT")
        || expr.contains("INTERLACED") || expr.contains("T") || expr.contains("POS") || expr.contains("PREV_INPTS") ||
                expr.contains("PREV_INT") || expr.contains("PREV_OUTPTS") || expr.contains("PREV_OUTT") ||
                expr.contains("RTCTIME") || expr.contains("RTCSTART") || expr.contains("TB") || expr.contains("T_CHANGE"))) {
            throw new InvalidArgumentException("The given expression must conform to FFmpeg's specifications regarding " +
                    "the \"setpts\" filter.", "L'espressione fornita deve essere conforme alle specifiche di FFmpeg " +
                    "riguardo il filtro \"setpts\".");
        }

        this.expr = expr;
    }

    @Override
    public void updateMap() {
        options.put("expr", expr);
    }
}
