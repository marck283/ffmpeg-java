package it.disi.unitn.transitions;

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

    /**
     * The picture produced by the translation.
     */
    protected BufferedImage img;

    /**
     * The path to the chosen temporary output directory.
     */
    protected String tempOutDir;

    /**
     * The Graphics2D instance chosen to translate the text.
     */
    protected Graphics2D g2d;

    /**
     * This class's constructor.
     * @param inputFile The path of the chosen input file.
     * @param tempOutDir The path to the chosen temporary output directory.
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
        MyFile myFile = new MyFile(tempOutDir + "/" + name + "." + fname);
        if(!ImageIO.write(img, fname, myFile)) {
            throw ex;
        }
    }

}
