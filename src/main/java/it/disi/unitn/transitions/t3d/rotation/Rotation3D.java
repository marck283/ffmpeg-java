package it.disi.unitn.transitions.t3d.rotation;

import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.awt.TextRenderer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Rotation3D {

    public void rotate(@NotNull String fontFamily, int style, int dim, @NotNull String text, float lx, float ly,
                       float lz, float scalingFactor) {
        TextRenderer textRenderer = new TextRenderer(new Font(fontFamily, style, dim));
        textRenderer.draw3D(text, lx, ly, lz, scalingFactor);
        ImageUtil util = ImageUtil.
    }

}
