package it.disi.unitn.transitions.rotation;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.RotationFailedException;
import it.disi.unitn.transitions.Transition;
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
        g2d.rotate(angle, anchorx, anchory);
        g2d.drawString(text,100F, 350F);

        try {
            savePicture(fname, name, new RotationFailedException("Picture rotation failed.", "Rotazione immagine non riuscita."));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
