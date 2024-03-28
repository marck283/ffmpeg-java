package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scalingalgs;

import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents the Bicubic scaling algorithm.
 */
public class Bicubic extends ScalingAlgorithm {

    /**
     * The class's constructor.
     * @param b The B parameter
     * @param c The C parameter
     */
    public Bicubic(double b, double c) throws InvalidArgumentException {
        super("bicubic");
        param0 = b;
        param1 = c;
    }

    /**
     * Returns a String representation of this object.
     * @return A String representation of this object
     */
    @Override
    public String toString() {
        return "bicubic:param0=" + param0 + ":param1=" + param1;
    }

}
