package it.disi.unitn.videocreator.filtergraph.filterchain.filters.multimedia.rotate.color;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * This class implements the colors needed for FFmpeg's "rotate" filter.
 */
public class Color {

    private @NotNull String colorName;

    /**
     * The class's constructor.
     */
    public Color() {
        colorName = "black";
    }

    /**
     * This method sets the color's name to a value that is recognizable by FFmpeg.
     * @param color The given color name. This value must not be null or an empty string, and it must be recognizable by
     *              FFmpeg (see <a href="https://ffmpeg.org/ffmpeg-utils.html#color-syntax">here</a> for the accepted
     *              color names).
     * @throws InvalidArgumentException If the given color's name is null, an empty string, or it is not recognizable by
     * FFmpeg.
     */
    public void setColor(@NotNull String color) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(color)) {
            throw new InvalidArgumentException("The given color cannot be null or an empty string.", "Il colore richiesto " +
                    "non puo' essere null o una stringa vuota.");
        }

        switch(color) {
            case "random", "none", "AliceBlue", "AntiqueWhite", "Aqua", "Aquamarine", "Azure", "Beige", "Bisque", "Black",
                 "BlanchedAlmond", "Blue", "BlueViolet", "Brown", "BurlyWood", "CadetBlue", "Chartreuse", "Chocolate",
                 "Coral", "CornflowerBlue", "Cornsilk", "Crimson", "Cyan", "DarkBlue", "DarkCyan", "DarkGoldenRod",
                 "DarkGray", "DarkGreen", "DarkKhaki", "DarkMagenta", "DarkOliveGreen", "Darkorange", "DarkOrchid",
                 "DarkRed", "DarkSalmon", "DarkSeaGreen", "DarkSlateBlue", "DarkSlateGray", "DarkTurquoise", "DarkViolet",
                 "DeepPink", "DeepSkyBlue", "DimGray", "DodgerBlue", "FireBrick", "FloralWhite", "ForestGreen", "Fuchsia",
                 "Gainsboro", "GhostWhite", "Gold", "GoldenRod", "Gray", "Green", "GreenYellow", "HoneyDew", "HotPink",
                 "IndianRed", "Indigo", "Ivory", "Khaki", "Lavender", "LavenderBlush", "LawnGreen", "LemonChiffon",
                 "LightBlue", "LightCoral", "LightCyan", "LightGoldenRodYellow", "LightGreen", "LightGrey", "LightPink",
                 "LightSalmon", "LightSeaGreen", "LightSkyBlue", "LightSlateGray", "LightSteelBlue", "LightYellow",
                 "Lime", "LimeGreen", "Linen", "Magenta", "Maroon", "MediumAquaMarine", "MediumBlue", "MediumOrchid",
                 "MediumPurple", "MediumSeaGreen", "MediumSlateBlue", "MediumSpringGreen", "MediumTurquoise",
                 "MediumVioletRed", "MidnightBlue", "MintCream", "MistyRose", "Mocassin", "NavajoWhite", "Navy", "OldLace",
                 "Olive", "OliveDrab", "Orange", "OrangeRed", "Orchid", "PaleGoldenRod", "PaleGreen", "PaleTurquoise",
                 "PaleVioletRed", "PapayaWhip", "PeachPuff", "Peru", "Pink", "Plum", "PowderBlue", "Purple", "Red",
                 "RosyBrown", "RoyalBlue", "SaddleBrown", "Salmon", "SandyBrown", "SeaGreen", "SeaShell", "Sienna",
                 "Silver", "SkyBlue", "SlateBlue", "SlateGray", "Snow", "SpringGreen", "SteelBlue", "Tan", "Teal",
                 "Thistle", "Tomato", "Turquoise", "Violet", "Wheat", "White", "WhiteSmoke", "Yellow", "YellowGreen" ->
                    this.colorName = color.toLowerCase(Locale.ROOT); //Match is case-insensitive in FFmpeg.
            default -> throw new InvalidArgumentException("Unrecognized color.", "Colore non riconosciuto.");
        }

    }

    /**
     * Returns the lowercase color name.
     * @return The lowercase color name. This value is guaranteed not to be null or an empty string.
     */
    @Override
    public String toString() {
        return colorName;
    }

}
