package it.disi.unitn.transitions;

import it.disi.unitn.exceptions.RotationFailedException;
import it.disi.unitn.lasagna.MyFile;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Base class for transitions.
 */
public class Transition {

    protected BufferedImage img;

    protected String tempOutDir;

    protected Graphics2D g2d;

    /**
     * This class's constructor.
     * @param inputFile The path of the chosen input file.
     */
    public Transition(@NotNull String inputFile, @NotNull String tempOutDir) {
        try {
            img = ImageIO.read(new MyFile(inputFile));
            g2d = (Graphics2D) img.getGraphics();

            this.tempOutDir = tempOutDir;
        } catch(IllegalArgumentException | IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Disposes the current Graphics2D instance.
     */
    public void dispose() {
        g2d.dispose();
    }

    /**
     * Saves the picture produced after the transition happened.
     * @param fname The file's name. This value cannot be null or an empty string.
     * @param name The file's extension. This value cannot be null or an empty string.
     * @param ex The Exception instance. This value cannot be null.
     * @throws Exception If the picture produced after the transition took place could not be saved.
     */
    public void savePicture(@NotNull String fname, @NotNull String name, @NotNull Exception ex) throws Exception {
        boolean res = ImageIO.write(img, fname, new MyFile(tempOutDir + "/" + name + "." + fname));
        if(!res) {
            throw ex;
        }
    }

}
