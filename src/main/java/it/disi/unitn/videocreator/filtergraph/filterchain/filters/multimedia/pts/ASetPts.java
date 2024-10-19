package it.disi.unitn.videocreator.filtergraph.filterchain.filters.multimedia.pts;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * This class implements the "asetpts" multimedia filter.
 */
public class ASetPts extends AudioFilter {

    private String expr;

    /**
     * This class's constructor.
     */
    public ASetPts() {
        super("asetpts");

        expr = "";
    }

    /**
     * This method sets the expression's value for the asetpts filter.
     * @param expr The given expression's value. This value cannot be null or an empty string, and it must conform to
     *             FFmpeg's asetpts filter specifications.
     * @throws InvalidArgumentException If the given value is null or an empty string
     */
    public void setExpr(@NotNull String expr) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(expr)) {
            throw new InvalidArgumentException("The given expression for the asetpts filter cannot be null or an empty " +
                    "string.", "L'espressione fornita per il filtro asetpts non puo' essere null o una stringa vuota.");
        }

        if(!(expr.contains("FRAME_RATE") || expr.contains("FR") || expr.contains("PTS") || expr.contains("N") ||
                expr.contains("NB_CONSUMED_SAMPLES") || expr.contains("NB_SAMPLES") || expr.contains("S") ||
                expr.contains("SAMPLE_RATE") || expr.contains("SR") || expr.contains("STARTPTS") || expr.contains("STARTT")
                || expr.contains("INTERLACED") || expr.contains("T") || expr.contains("POS") || expr.contains("PREV_INPTS")
                || expr.contains("PREV_INT") || expr.contains("PREV_OUTPTS") || expr.contains("PREV_OUTT") ||
                expr.contains("RTCTIME") || expr.contains("RTCSTART") || expr.contains("TB") || expr.contains("T_CHANGE"))) {
            throw new InvalidArgumentException("The given expression must conform to FFmpeg's specifications regarding " +
                    "the \"setpts\" filter.", "L'espressione fornita deve essere conforme alle specifiche di FFmpeg " +
                    "riguardo il filtro \"setpts\".");
        }

        this.expr = expr;
    }

    @Override
    public void updateMap() {
        if(expr.isEmpty()) {
            Locale l = Locale.getDefault();
            if(l == Locale.ITALIAN || l == Locale.ITALY) {
                System.err.println("L'espressione fornita per il filtro asetpts non puo' essere una stringa vuota se il " +
                        "filtro sta per essere impostato.");
            } else {
                System.err.println("The expression's value for the asetpts filter cannot be empty if the filter is being " +
                        "set.");
            }
            System.exit(2);
        }
        options.put("expr", expr);
    }
}
