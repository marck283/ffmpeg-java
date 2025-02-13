package it.disi.unitn.transitions.t2d.translation;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.transitions.TranslationFailedException;
import it.disi.unitn.transitions.t2d.Transition2D;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * This class performs the underlying work to handle the translation.
 */
class Translation extends Transition2D {

    /**
     * The class's constructor.
     * @param inputFile The given input file's path.
     * @param tempOutDir The path to the temporary output directory.
     */
    public Translation(@NotNull String inputFile, @NotNull String tempOutDir) {
        super(inputFile, tempOutDir);
    }

    /**
     * This method handles the rotation process.
     * @param start The Point2D instance that denotes the transition's starting point. This value cannot be null and none
     *              of its coordinates can be negative.
     * @param text The string to be drawn. This value cannot be null or an empty string.
     * @param fname The output file's extension. This value cannot be null or an empty string.
     * @param name The output file's name. This value cannot be null or an empty string.
     * @param fontFamily The family of the font to be used. This value cannot be an empty string, but it can be null, in
     *                   which case it is set according to {@code java.awt.Font}'s specification.
     * @param fontStyle An integer denoting the font's style. This value must be set according to {@code java.awt.Font}'s
     *                  specification.
     * @param fontSize The font's size. This value cannot be null or negative.
     * @param fontColor The font's color. This parameter cannot be null.
     * @throws InvalidArgumentException If any of the above requirements is not met.
     */
    public void translate(@NotNull Point2D start, double distX, double distY, @NotNull String text, @NotNull String fname,
                          @NotNull String name, @NotNull String fontFamily, int fontStyle, int fontSize,
                          @NotNull Color fontColor) throws InvalidArgumentException {
        if(start == null) {
            throw new InvalidArgumentException("None of the arguments given to the translation transition can be null.",
                    "Nessuno degli argomenti forniti alla traslazione puo' essere null.");
        }

        if(start.getX() < 0 || start.getY() < 0) {
            throw new InvalidArgumentException("None of the coordinates of the input points can be negative.", "Nessuna " +
                    "delle coordinate dei punti in input puo' essere negativa.");
        }

        if(StringExt.checkNullOrEmpty(text)) {
            throw new InvalidArgumentException("The string to be printed on screen cannot be null or an empty string.",
                    "La stringa da stampare sullo schermo non puo' essere null o una stringa vuota.");
        }

        if(StringExt.checkNullOrEmpty(fname)) {
            throw new InvalidArgumentException("The output file's extension cannot be null or an empty string.", "L'estensione " +
                    "del file di output non puo' essere null o una stringa vuota.");
        }

        if(StringExt.checkNullOrEmpty(name)) {
            throw new InvalidArgumentException("The output file's name cannot be null or an empty string.", "Il nome del " +
                    "file di output non puo' essere null o una stringa vuota.");
        }

        if(fontFamily.isEmpty()) {
            throw new InvalidArgumentException("The given font's family cannot be an empty string.", "La famiglia del font " +
                    "non puo' essere una stringa vuota.");
        }

        if(fontSize <= 0) {
            throw new InvalidArgumentException("The given font's size cannot be null or negative.", "La dimensione del " +
                    "font richiesto non puo' essere minore o uguale a zero.");
        }

        if(fontColor == null) {
            throw new InvalidArgumentException("The given font's color cannot be null.", "Il colore del font richiesto " +
                    "non puo' essere null.");
        }

        g2d.setFont(new Font(fontFamily, fontStyle, fontSize));
        g2d.setColor(fontColor);

        //Perform 2D translation
        //g2d.translate(distX, distY);
        g2d.drawString(text, (float)(start.getX() + distX), (float)(start.getY() + distY));

        try {
            savePicture(fname, name, new TranslationFailedException("Text translation failed.", "Traslazione testo non riuscita."));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}