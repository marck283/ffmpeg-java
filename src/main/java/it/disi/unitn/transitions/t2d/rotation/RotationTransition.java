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
     * @param fname The output file's extension.
     * @param fext The intermediate files' extension.
     * @param angle The rotation's angle.
     * @throws InvalidArgumentException If the given angle is negative or greater than 999.
     */
    public RotationTransition(@NotNull String inputFile, @NotNull String videoOutDir, @NotNull String tempOutDir,
                              @NotNull String fname, @NotNull String fext, int angle) throws InvalidArgumentException {
        super(inputFile, tempOutDir, videoOutDir);
        if(angle < 0 || angle > 999) {
            throw new InvalidArgumentException("The given angle cannot be less than 0 or greater than 999 degrees.",
                    "L'angolo fornito non puo' essere minore di 0 o maggiore di 999 gradi.");
        }
        this.fname = fname;
        this.fext = fext;
        this.angle = angle;
    }

    /**
     * This method performs the rotation.
     * @param anchorx The x-value of the anchor.
     * @param anchory The y-value of the anchor.
     * @param text The text to be writer.
     * @param fontFamily The given font's family. This value cannot be null or an empty string.
     * @param fontStyle The given font's style.
     * @param fontSize The given font's size. This value cannot be less than or equal to zero.
     * @param color The chosen Color instance
     * @throws InvalidArgumentException If one of the non-null arguments is null or an empty string
     */
    public void rotate(int anchorx, int anchory, @NotNull String text,
                       @NotNull String fontFamily, int fontStyle, int fontSize, @NotNull Color color) throws Exception {
        int j = 0;
        for(int i = 0; i < angle; i++) {
            if((i + 1)%10 == 0) {
                //Save intermediate video and delete the folder's content.
                StringExt strExt = new StringExt(String.valueOf(j));
                strExt.padStart(3);
                performTransition(1L, TimeUnit.MINUTES, strExt.getVal(), fname, false);

                MyFile tempDir = new MyFile(tempOutDir);
                tempDir.removeContent(fext);
            }

            StringExt str1 = new StringExt(String.valueOf(i));
            str1.padStart(3);

            rotation = new Rotation(inputFile, tempOutDir);
            rotation.rotate(anchorx, anchory, Math.toRadians(j + 1), text, str1.getVal(), fext, fontFamily, fontStyle,
                    fontSize, color);
            j++;
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

            tempDir = new MyFile(videoOutDir);
            tempDir.removeContentExceptFile("output");
        } catch(IOException | InvalidArgumentException ex) {
            ex.printStackTrace();
        }
    }

}
