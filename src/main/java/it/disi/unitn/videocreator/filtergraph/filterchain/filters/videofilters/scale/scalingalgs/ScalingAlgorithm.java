package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.scalingalgs;

import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a general scaling algorithm. It is not meant to be used in production. Please use one of its
 * derivatives instead.
 */
public abstract class ScalingAlgorithm {

    private final String algname;

    /**
     * These parameters can sometimes be given to the scaling algorithm (depending on the algorithm).
     */
    protected double param0, param1;

    /**
     * The class's constructor.
     * @param name The name of the scaling algorithm
     * @throws InvalidArgumentException If the name of the given scaling algorithm is null or an empty string
     */
    public ScalingAlgorithm(@NotNull String name) throws InvalidArgumentException {
        if(name == null || name.isEmpty()) {
            throw new InvalidArgumentException("The name of the scaling algorithm cannot be null or an empty string.",
                    "Il nome dello scaling algorithm non puo' essere null o una stringa vuota.");
        }
        algname = name;
    }

    /**
     * Gets the name of the scaling algorithm.
     * @return The name of the scaling algorithm
     */
    public String getName() {
        return algname;
    }

    /**
     * Returns a String representation of this ScalingAlgorithm instance.
     * @return A String representation of this ScalingAlgorithm instance
     */
    @Override
    public abstract String toString();

}
