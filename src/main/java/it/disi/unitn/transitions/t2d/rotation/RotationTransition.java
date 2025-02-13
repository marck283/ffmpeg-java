package it.disi.unitn.transitions.t2d.rotation;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.lasagna.MyFile;
import it.disi.unitn.transitions.t2d.TransitionParent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * This class performs the rotation.
 */
final public class RotationTransition extends TransitionParent {

    private Rotation rotation;

    private final String fname, fext;

    private final int angle;

    /**
     * The class's constructor. None of the given values can be null or empty strings.
     * @param inputFile The given input file's path.
     * @param videoOutDir The temporary video output directory's path.
     * @param tempOutDir The temporary picture output directory's path.
     * @param tempVideoDir The temporary videos' output directory's path.
     * @param fname The output file's extension.
     * @param fext The intermediate files' extension.
     * @param angle The rotation's angle. This value cannot be greater than 999. As usual, a positive value ensures a
     *              clockwise rotation, while a negative one ensures a counterclockwise rotation.
     * @throws InvalidArgumentException If the given angle is negative or greater than 999.
     */
    public RotationTransition(@NotNull String inputFile, @NotNull String videoOutDir, @NotNull String tempOutDir,
                              @NotNull String tempVideoDir, @NotNull String fname, @NotNull String fext, int angle)
            throws InvalidArgumentException {
        super(inputFile, tempOutDir, tempVideoDir, videoOutDir, fname);
        if(angle > 999) {
            throw new InvalidArgumentException("The given angle cannot be greater than 999 degrees.",
                    "L'angolo fornito non puo' essere maggiore di 999 gradi.");
        }
        this.fname = fname;
        this.fext = fext;
        this.angle = angle;
        rotation = new Rotation(inputFile, tempOutDir);
    }

    /**
     * This method performs the rotation.
     * @param anchorx The x-value of the anchor.
     * @param anchory The y-value of the anchor.
     * @param x The x-coordinate at which the string will be drawn.
     * @param y The y-coordinate at which the string will be drawn.
     * @param text The text to be writer.
     * @param fontFamily The given font's family. This value cannot be null or an empty string.
     * @param fontStyle The given font's style.
     * @param fontSize The given font's size. This value cannot be less than or equal to zero.
     * @param color The chosen Color instance
     * @throws InvalidArgumentException If one of the non-null arguments is null or an empty string
     */
    public void rotate(int anchorx, int anchory, float x, float y, @NotNull String text,
                       @NotNull String fontFamily, int fontStyle, int fontSize, @NotNull Color color) throws Exception {
        int j = 0;
        for(int i = 0; i < Math.abs(angle); i++) {
            if(i != 0 && i%10 == 0) {
                //Save intermediate video and delete the folder's content.
                StringExt strExt = new StringExt(String.valueOf(Math.abs(j)));
                strExt.padStart(3);
                performTransition(1L, TimeUnit.MINUTES, strExt.getVal(), false, true);

                MyFile tempDir = new MyFile(tempOutDir);
                tempDir.removeContent(fext);
            }

            StringExt str1 = new StringExt(String.valueOf(i));
            str1.padStart(3);

            rotation = new Rotation(inputFile, tempOutDir);
            rotation.rotate(anchorx, anchory, x, y, Math.toRadians(j), text, str1.getVal(), fext, fontFamily, fontStyle,
                    fontSize, color);
            if(angle > 0) {
                j++;
            } else {
                j--;
            }
        }
    }

    /**
     * This method disposes of the underlying objects used in this class.
     */
    public void dispose() {
        try {
            rotation.dispose();
            MyFile tempDir = new MyFile(tempOutDir);
            tempDir.removeContent(fext);

            MyFile tempVideoFile = new MyFile(tempVideoDir);
            tempVideoFile.removeContent(fname);

            tempDir = new MyFile(videoOutDir);
            tempDir.removeContentExceptFile("output");
        } catch(IOException | InvalidArgumentException ex) {
            ex.printStackTrace();
        }
    }

}
