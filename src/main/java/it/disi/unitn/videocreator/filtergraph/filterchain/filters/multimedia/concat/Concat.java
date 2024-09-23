package it.disi.unitn.videocreator.filtergraph.filterchain.filters.multimedia.concat;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;

/**
 * This class implements the "concat" multimedia filter.
 */
public class Concat extends Filter {

    private int n, v, a;

    /**
     * This class's constructor. Constructs a new filter (whether video or audio).
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public Concat() throws InvalidArgumentException {
        super("concat");
        n = 2;
        v = 1;
        a = 0;
    }

    /**
     * Sets the "n" parameter.
     * @param n The given "n" parameter's value
     * @throws InvalidArgumentException If the given value is less than or equal to zero
     */
    public void setN(int n) throws InvalidArgumentException {
        if(n <= 0) {
            throw new InvalidArgumentException("The \"n\" parameter cannot be less than or equal to zero.", "Il parametro " +
                    "\"n\" non puo' essere minore o uguale a zero.");
        }

        this.n = n;
    }

    /**
     * Sets the "v" parameter.
     * @param v The given "v" parameter's value
     * @throws InvalidArgumentException If the given value is less than or equal to zero
     */
    public void setV(int v) throws InvalidArgumentException {
        if(v <= 0) {
            throw new InvalidArgumentException("The \"v\" parameter cannot be less than or equal to zero.", "Il parametro " +
                    "\"v\" non puo' essere minore o uguale a zero.");
        }

        this.v = v;
    }

    /**
     * Sets the "a" parameter.
     * @param a The given "a" parameter's value
     * @throws InvalidArgumentException If the given value is less than zero
     */
    public void setA(int a) throws InvalidArgumentException {
        if(a < 0) {
            throw new InvalidArgumentException("The \"a\" parameter cannot be less than to zero.", "Il parametro " +
                    "\"a\" non puo' essere minore di zero.");
        }

        this.a = a;
    }

    @Override
    public void updateMap() {
        options.put("n", String.valueOf(n));
        options.put("v", String.valueOf(v));
        options.put("a", String.valueOf(a));
    }
}
