package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.drawtext.boxborderw;

import it.disi.unitn.exceptions.InvalidArgumentException;

/**
 * This class implements the "boxborderw" option of the "drawtext" video filter.
 */
public class BoxBorderW {

    private int top, bottom, left, right;

    /**
     * The class's constructor.
     */
    public BoxBorderW() {
        top = 0;
        bottom = 0;
        left = 0;
        right = 0;
    }

    /**
     * Sets the top border's value.
     * @param top The new top border's value. Cannot be negative.
     * @throws InvalidArgumentException If the given top border's value is negative
     */
    public void setTop(int top) throws InvalidArgumentException {
        if(top < 0) {
            throw new InvalidArgumentException("The top border's value cannot be less than 0.", "Il valore del bordo " +
                    "superiore non puo' essere negativo.");
        }
        this.top = top;
    }

    /**
     * Sets the bottom border's value.
     * @param bottom The new bottom border's value. Cannot be negative.
     * @throws InvalidArgumentException If the given bottom border's value is negative
     */
    public void setBottom(int bottom) throws InvalidArgumentException {
        if(bottom < 0) {
            throw new InvalidArgumentException("The bottom border's value cannot be less than 0.", "Il valore del bordo " +
                    "inferiore non puo' essere negativo.");
        }
        this.bottom = bottom;
    }

    /**
     * Sets the left border's value.
     * @param left The new left border's value. Cannot be negatve.
     * @throws InvalidArgumentException If the given left border's value is negative
     */
    public void setLeft(int left) throws InvalidArgumentException {
        if(left < 0) {
            throw new InvalidArgumentException("The left border's value cannot be less than 0.", "Il valore del bordo " +
                    "sinistro non puo' essere negativo.");
        }

        this.left = left;
    }

    /**
     * Sets the right border's value.
     * @param right The new right border's value. Cannot be negative.
     * @throws InvalidArgumentException If the given right border's value is negative
     */
    public void setRight(int right) throws InvalidArgumentException {
        if(right < 0) {
            throw new InvalidArgumentException("The right border's value cannot be less than 0.", "Il valore del bordo " +
                    "destro non puo' essere negativo.");
        }

        this.right = right;
    }

    public String toString() {
        return top + "|" + bottom + "|" + left + "|" + right;
    }

}
