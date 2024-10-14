package it.disi.unitn.transitions.rotation;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.RotationFailedException;
import it.disi.unitn.lasagna.File;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

class Rotation {

    private final BufferedImage img;

    private final String tempOutdir;

    private final Graphics2D g2d;

    public Rotation(@NotNull String inputFile, @NotNull String tempOutdir) throws IOException {
        img = ImageIO.read(new File(inputFile));
        g2d = (Graphics2D) img.getGraphics();

        this.tempOutdir = tempOutdir;
    }

    @Contract("_, _, _, _ -> new")
    private @NotNull Pair<Double, Double> getAnchors(@NotNull String text, double angle, double anchorx, double anchory) {
        FontMetrics fm = g2d.getFontMetrics();
        int strWidth = fm.stringWidth(text);

        angle = angle/57.2958;
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

    void rotate(double anchorx, double anchory, double angle, @NotNull String text, @NotNull String name,
                       @NotNull String fname, @NotNull Color color) throws IOException, RotationFailedException,
            InvalidArgumentException {
        if(angle < 0D) {
            throw new InvalidArgumentException("The angle's value cannot be negative.", "L'angolo indicato non puo' " +
                    "essere negativo.");
        }
        g2d.setFont(new Font("Arial Unicode MS", Font.PLAIN, 200));
        g2d.setColor(color);

        Pair<Double, Double> pair = getAnchors(text, angle, anchorx, anchory);

        g2d.rotate(angle, pair.getLeft(), pair.getRight());
        g2d.drawString(text,100F, 350F);

        boolean res = ImageIO.write(img, fname, new File(tempOutdir + "/" + name + "." + fname));
        if(!res) {
            throw new RotationFailedException("Picture rotation failed.", "Rotazione immagine non riuscita.");
        }
    }

    void dispose() {
        g2d.dispose();
    }
}
