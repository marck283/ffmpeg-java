package it.disi.unitn.transitions.rotation;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.RotationFailedException;
import it.disi.unitn.transitions.Transition;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * This class performs the underlying work to handle the rotation.
 */
class Rotation extends Transition {

    /**
     * This class's constructor
     * @param inputFile The given input file's path.
     * @param tempOutDir The path to the temporary output directory.
     */
    public Rotation(@NotNull String inputFile, @NotNull String tempOutDir) {
        super(inputFile, tempOutDir);
    }

    @Contract("_, _, _, _ -> new")
    private @NotNull Pair<Double, Double> getAnchors(@NotNull String text, double angle, double anchorx, double anchory) {
        FontMetrics fm = g2d.getFontMetrics();
        int strWidth = fm.stringWidth(text);

        double interAngle = angle%360D;
        if(interAngle <= 180D) {
            anchory -= strWidth*Math.sin(angle)/2;

            if(interAngle < 90D) {
                anchorx -= strWidth*Math.cos(angle)/2;
            } else {
                anchorx += strWidth*Math.sin(angle)/2;
            }
        } else {
            anchory += strWidth*Math.sin(angle)/2;

            if(interAngle <= 270D) {
                anchorx += strWidth*Math.cos(angle)/2;
            } else {
                anchorx += strWidth*Math.sin(angle)/2;
            }
        }

        return new ImmutablePair<>(anchorx, anchory);
    }

    /**
     * This method rotates the text.
     * @param anchorx The x-axis anchor.
     * @param anchory The y-axis anchor.
     * @param angle The given angle in degrees. This value cannot be negative.
     * @param text The text to be rotated.
     * @param name The output file's name.
     * @param fname The output file's extension.
     * @param color The font's color.
     * @throws InvalidArgumentException If the given angle is negative
     */
    void rotate(double anchorx, double anchory, double angle, @NotNull String text, @NotNull String name,
                       @NotNull String fname, @NotNull Color color) throws InvalidArgumentException {
        if(angle < 0D) {
            throw new InvalidArgumentException("The angle's value cannot be negative.", "L'angolo indicato non puo' " +
                    "essere negativo.");
        }
        g2d.setFont(new Font("Arial Unicode MS", Font.PLAIN, 200));
        g2d.setColor(color);

        angle /= 57.2958D;

        Pair<Double, Double> pair = getAnchors(text, angle, anchorx, anchory);

        g2d.rotate(angle, pair.getLeft(), pair.getRight());
        g2d.drawString(text,100F, 350F);

        try {
            savePicture(fname, name, new RotationFailedException("Picture rotation failed.", "Rotazione immagine non riuscita."));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
