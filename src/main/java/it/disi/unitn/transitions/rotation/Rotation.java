package it.disi.unitn.transitions.rotation;

import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.RotationFailedException;
import it.disi.unitn.lasagna.File;
import it.disi.unitn.videocreator.VideoCreator;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

class Rotation {

    private final BufferedImage img;

    private final String tempOutdir;

    private final Graphics2D g2d;

    public Rotation(@NotNull String inputFile, @NotNull String tempOutdir) throws IOException {
        img = ImageIO.read(new File(inputFile));
        g2d = (Graphics2D) img.getGraphics();

        this.tempOutdir = tempOutdir;
    }

    void rotate(int anchorx, int anchory, double angle, @NotNull String text, @NotNull String name,
                       @NotNull String fname, @NotNull Color color) throws IOException, RotationFailedException {
        g2d.setFont(new Font("Arial Unicode MS", Font.PLAIN, 200));
        g2d.setColor(color);
        g2d.rotate(angle/57.2958, anchorx, anchory);
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
