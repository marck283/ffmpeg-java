package it.disi.unitn.transitions.t2d.translation;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.lasagna.MyFile;
import it.disi.unitn.transitions.t2d.TransitionParent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * This class handles the translation of text.
 */
final public class TranslationTransition extends TransitionParent {

    private Translation translation;

    private final double distanceX, theta;

    /**
     * This class's constructor.
     * @param inputFile The given input file. This value must not be null or an empty string.
     * @param tempOutDir The given output directory for the intermediate files. This value must not be null or an empty
     *                   string.
     * @param videoOutDir The video's output directory. This value cannot be null or an empty string.
     * @param tempVideoDir The temporary video's output directory. This value cannot be null or an empty string.
     * @param outName The output file's name. This value cannot be null or an empty string.
     * @param fname The temporary files' extension. This value cannot be null or an empty string.
     * @param distx The distance on the x-axis. This value can be positive (right translation), negative (left translation), or null.
     * @param theta The translation angle in degrees. This value can be positive (downwards translation), negative (upwards
     *              translation), or null. This value cannot be greater than 999.
     * @param fext The intermediate files' extension. This value cannot be null or an empty string.
     * @throws InvalidArgumentException If any of the given arguments (except {@code theta}) does not meet its requirements
     */
    public TranslationTransition(@NotNull String inputFile, @NotNull String tempOutDir, @NotNull String videoOutDir,
                                 @NotNull String tempVideoDir, @NotNull String outName, @NotNull String fname,
                                 double distx, double theta, @NotNull String fext) throws InvalidArgumentException {
        super(inputFile, tempOutDir, tempVideoDir, videoOutDir, fname, outName, fext);
        if(StringExt.checkNullOrEmpty(inputFile) || StringExt.checkNullOrEmpty(tempOutDir) ||
                StringExt.checkNullOrEmpty(videoOutDir) || StringExt.checkNullOrEmpty(tempVideoDir) ||
        StringExt.checkNullOrEmpty(fname) || StringExt.checkNullOrEmpty(fext)) {
            throw new InvalidArgumentException("None of the arguments given to TranslationTransition can be null or empty " +
                    "strings.", "Nessuno degli argomenti forniti a TranslationTransition puo' essere null o una stringa " +
                    "vuota.");
        }
        if(distx < 0) {
            throw new InvalidArgumentException("The given distance on the x-axis cannot be negative.", "La distanza " +
                    "fornita sull'asse delle ascisse non puo' essere negativa.");
        }
        this.distanceX = distx;
        this.theta = Math.toRadians(theta);
        this.fext = fext;
    }

    private void translationLoop(double distX, double distY, int i, @NotNull Point2D start, @NotNull String text,
                                 @NotNull String fontFamily, int fontStyle, int fontSize, @NotNull Color fontColor)
            throws Exception {
        if(i != 0 && i%10 == 0) {
            //Save intermediate video and delete the folder's content.
            StringExt strExt = new StringExt(String.valueOf(i));
            strExt.padStart(3);
            setOutName(strExt.getVal());
            performTransition(1L, TimeUnit.MINUTES, false, true);

            MyFile tempDir = new MyFile(tempOutDir);
            tempDir.removeContent(fext);
        }

        StringExt strExt = new StringExt(String.valueOf(i));
        strExt.padStart(3);

        translation = new Translation(inputFile, tempOutDir);
        translation.translate(start, distX, distY, text, fext, strExt.getVal(), fontFamily, fontStyle, fontSize, fontColor);
    }

    /**
     * This method performs the translation.
     * @param start The translation's starting point. This value cannot be null.
     * @param text The string to be translated. This value cannot be null or an empty string.
     * @param fontFamily The string representing the required font's family. This value cannot be null or an empty string.
     * @param fontStyle The required font's style.
     * @param fontSize The required font's size in points. This value cannot be negative or null.
     * @param fontColor The required font's color. This value cannot be null.
     * @throws Exception If an exception occurs.
     */
    public void translate(@NotNull Point2D start, @NotNull String text,
                          @NotNull String fontFamily, int fontStyle, int fontSize, @NotNull Color fontColor) throws Exception {
        if(start == null || StringExt.checkNullOrEmpty(text) ||
                StringExt.checkNullOrEmpty(fontFamily) || fontSize <= 0 || fontColor == null) {
            throw new InvalidArgumentException("None of the given arguments can be null, an empty string, negative or " +
                    "the null number.", "Nessuno degli argomenti forniti puo' essere null, una stringa vuota, negativo o" +
                    "pari a zero.");
        }
        double distX = 0D, distY = 0D, distanceY = 0.0D;

        if(theta%(Math.PI/2) != 0) {
            distanceY = Math.pow(distanceX*Math.tan(theta), 2.0D);
        }

        int i = 0;
        double distance = Math.sqrt(Math.pow(distanceX, 2.0D) + distanceY);
        while(Math.sqrt(Math.pow(distX, 2.0D) + Math.pow(distY, 2.0D)) <= distance) {
            translationLoop(distX, distY, i, start, text, fontFamily, fontStyle, fontSize, fontColor);

            distX += Math.cos(theta);
            distY += Math.sin(theta);

            i++;
        }

        MyFile toutDir = new MyFile(tempOutDir);
        java.util.List<String> pathList = toutDir.getFileList();
        if(!pathList.isEmpty()) {
            StringExt strExt = new StringExt(String.valueOf(i));
            strExt.padStart(3);
            setOutName(strExt.getVal());
            performTransition(1L, TimeUnit.MINUTES, false, true);
        }
    }

    /**
     * This method disposes of the underlying objects used in this class.
     */
    public void dispose() {
        try {
            translation.dispose();
            super.dispose();
        } catch(IOException | InvalidArgumentException ex) {
            ex.printStackTrace();
        }
    }

}
