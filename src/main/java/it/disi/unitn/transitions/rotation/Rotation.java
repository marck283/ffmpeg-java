package it.disi.unitn.transitions.rotation;

import it.disi.unitn.StringExt;
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

    /**
     * This method allows the text's rotation given an angle in degrees, and the x and y anchors.
     * @param text The given text. This value cannot be null or an empty string.
     * @param angle The given angle.
     * @param anchorx The given x-anchor. This value cannot be less than 0.
     * @param anchory The given y-anchor. This value cannot be less than 0.
     * @return A (x-anchor, y-anchor) pair. This pair is an instance of ImmutablePair.
     * @throws InvalidArgumentException If one of the given anchor values is less than 0, as that would mean that the
     * given text would be rotated on a point beyond the frame's bounds.
     */
    @Contract("_, _, _, _ -> new")
    private @NotNull ImmutablePair<Double, Double> getAnchors(@NotNull String text, double angle, double anchorx,
                                                              double anchory) throws InvalidArgumentException {
        if(anchorx < 0 || anchory < 0) {
            throw new InvalidArgumentException("The given anchors cannot be less than 0.", "I valori forniti per il " +
                    "perno della rotazione non possono essere minori di 0.");
        }

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
     * @param anchorx The x-axis anchor. This value cannot be negative.
     * @param anchory The y-axis anchor. This value cannot be negative.
     * @param angle The given angle in degrees. This value cannot be negative.
     * @param text The text to be rotated. This value cannot be null or an empty string.
     * @param name The output file's name. This value cannot be null or an empty string.
     * @param fname The output file's extension. This value cannot be null or an empty string.
     * @param fontFamily The given font's family. This value cannot be null or an empty string.
     * @param fontStyle The given font's style.
     * @param fontSize The given font's size. This value cannot be less than or equal to zero.
     * @param fontColor The font's color. This value cannot be null.
     * @throws InvalidArgumentException If any of the above parameters' values does not conform to the given specifications
     */
    void rotate(double anchorx, double anchory, double angle, @NotNull String text, @NotNull String name,
                       @NotNull String fname, @NotNull String fontFamily, int fontStyle, int fontSize,
                @NotNull Color fontColor)
            throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(text)) {
            throw new InvalidArgumentException("The given text cannot be null or an empty string.", "Il testo fornito " +
                    "non puo' essere null o una stringa vuota.");
        }

        if(StringExt.checkNullOrEmpty(name)) {
            throw new InvalidArgumentException("The output file's name cannot be null or an empty string.", "Il nome " +
                    "fornito per il file risultante non puo' essere null o una stringa vuota.");
        }

        if(StringExt.checkNullOrEmpty(fname)) {
            throw new InvalidArgumentException("The output file's extension cannot be null or an empty string.",
                    "L'estensione del file risultante fornita non puo' essere null o una stringa vuota.");
        }

        if(angle < 0D) {
            throw new InvalidArgumentException("The angle's value cannot be negative.", "L'angolo indicato non puo' " +
                    "essere negativo.");
        }

        if(fontSize <= 0) {
            throw new InvalidArgumentException("The given font size cannot be less than or equal to zero.", "La dimensione " +
                    "fornita per il font scelto non puo' essere minore o uguale a zero.");
        }

        g2d.setFont(new Font(fontFamily, fontStyle, fontSize));
        g2d.setColor(fontColor);

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
