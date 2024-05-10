package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.drawtext;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.drawtext.boxborderw.BoxBorderW;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.drawtext.timecode.TimeCode;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * This class implements the "drawtext" filter.
 */
public class DrawText extends VideoFilter {

    private int box, basetime, fontsize, text_shaping, boxw, boxh, shadowx, shadowy, start_number, tabsize, tc24hmax,
    reload;

    private BoxBorderW boxborderw;

    private String boxcolor, text_align, y_align, bordercolor, expansion, fontcolor, fontcolor_expr, font, fontfile,
            ft_load_flags, shadowcolor, text, textfile, text_source, x, y;

    private double line_spacing, borderw, alpha;

    private boolean fix_bounds;

    private TimeCode timecode;

    /**
     * This class's constructor. Constructs a new video filter.
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public DrawText() throws InvalidArgumentException {
        super("drawtext");
        box = 0;
        boxborderw = new BoxBorderW();
        boxcolor = "white";
        line_spacing = 0.0;
        text_align = "";
        y_align = "text";
        bordercolor = "black";
        expansion = "normal";
        basetime = 0;
        fix_bounds = false;
        fontcolor = "black";
        fontcolor_expr = "";
        font = "Sans";
        fontfile = "";
        alpha = 1.0;
        fontsize = 16;
        text_shaping = 1;
        ft_load_flags = "default";
        boxw = 0;
        boxh = 0;
        shadowx = 0;
        shadowy = 0;
        start_number = 0;
        tabsize = 4;
        timecode = new TimeCode();
        tc24hmax = 0;
        text = "";
        textfile = "";
        text_source = "";
        reload = 0;
        x = "0";
        y = "0";
    }

    /**
     * Enables or disables the box around the drawn text.
     * @param box Enables the box if set to 1, disables it if set to 0.
     * @throws InvalidArgumentException If the given parameter is set to a value that is not 0 nor 1
     */
    public void setBox(int box) throws InvalidArgumentException {
        if(box != 0 && box != 1) {
            throw new InvalidArgumentException("The \"box\" value must be either 0 or 1.", "Il valore \"box\" deve essere 0 " +
                    "o 1.");
        }

        this.box = box;
    }

    /**
     * Sets the "boxborderw" option.
     * @param boxborderw The given BoxBorderW instance.
     */
    public void setBoxBorderW(BoxBorderW boxborderw) {
        this.boxborderw = Objects.requireNonNullElseGet(boxborderw, BoxBorderW::new);
    }

    /**
     * Sets the "boxcolor" option's value.
     * @param boxcolor The new "boxcolor" option's value. This value must be compatible with the syntax required by FFmpeg.
     * @see <a href="https://ffmpeg.org/ffmpeg-utils.html#color-syntax">https://ffmpeg.org/ffmpeg-utils.html#color-syntax</a>
     * for the list of accepted values
     * @throws InvalidArgumentException If the given value is null or an empty string
     */
    public void setBoxColor(@NotNull String boxcolor) throws InvalidArgumentException {
        if(checkNullOrEmpty(boxcolor)) {
            throw new InvalidArgumentException("The given \"boxcolor\" value cannot be null or empty.", "Il valore " +
                    "fornito per \"boxcolor\" non puo' essere null o una stringa vuota.");
        }

        this.boxcolor = boxcolor;
    }

    /**
     * Sets the "line_spacing" option's value.
     * @param line_spacing The new value of the "line_spacing" option. This value cannot be less than 0.0.
     * @throws InvalidArgumentException If the "line_spacing" option's value is less than 0.0
     */
    public void setLineSpacing(double line_spacing) throws InvalidArgumentException {
        if(line_spacing < 0.0) {
            throw new InvalidArgumentException("The \"line spacing\" value cannot be negative.", "Il valore dell'opzione " +
                    "\"line_spacing\" non puo' essere negativo.");
        }

        this.line_spacing = line_spacing;
    }

    /**
     * Sets the "text_align" option's value.
     * @param text_align The new "text_align" option's value. This value cannot be null or an empty string.
     * @throws InvalidArgumentException If the given value is null or an empty string
     * @see <a href="https://ffmpeg.org/ffmpeg-filters.html">https://ffmpeg.org/ffmpeg-filters.html</a> for further
     *      information about the accepted values
     */
    public void setTextAlign(@NotNull String text_align) throws InvalidArgumentException {
        if(checkNullOrEmpty(text_align)) {
            throw new InvalidArgumentException("The \"text_align\" value cannot be null or empty.", "Il valore dell'opzione " +
                    "\"text_align\" non puo' essere null o una stringa vuota.");
        }

        this.text_align = text_align;
    }

    /**
     * Sets the "y_align" option's value.
     * @param y_align The new "y_align" option's value. This value must be equal to "text", "baseline" or "font".
     * @throws InvalidArgumentException If the given "y_align" option's value is different from "text", "baseline" or
     *         "font"
     */
    public void setYAlign(@NotNull String y_align) throws InvalidArgumentException {
        if(checkNullOrEmpty(y_align)) {
            throw new InvalidArgumentException("The \"y_align\" value cannot be null or an empty string.", "Il valore " +
                    "dell'opzione \"y_align\" non puo' essere null o una stringa vuota.");
        }

        switch(y_align) {
            case "text", "baseline", "font" -> this.y_align = y_align;
            default -> throw new InvalidArgumentException("The \"y_align\" value must be equal to \"text\", \"baseline\" or " +
                    "\"font\".", "Il valore dell'opzione \"y_align\" deve essere uguale a \"text\", \"baseline\" o \"font\".");
        }
    }

    /**
     * Sets the "borderw" option's value.
     * @param borderw The new "borderw" option's value. This value cannot be negative.
     * @throws InvalidArgumentException If the given value is negative
     */
    public void setBorderW(double borderw) throws InvalidArgumentException {
        if(borderw < 0.0) {
            throw new InvalidArgumentException("The \"borderw\" option's value cannot be negative.", "Il valore dell'opzione " +
                    "\"borderw\" non puo' essere negativo.");
        }

        this.borderw = borderw;
    }

    /**
     * Sets the "bordercolor" option's value.
     * @param bordercolor The new "bordercolor" option's value. This value must be one of the values accepted by FFmpeg.
     * @throws InvalidArgumentException If the given value is null or an empty string
     * @see <a href="https://ffmpeg.org/ffmpeg-utils.html#color-syntax">https://ffmpeg.org/ffmpeg-utils.html#color-syntax</a>
     * for the list of accepted values
     */
    public void setBorderColor(@NotNull String bordercolor) throws InvalidArgumentException {
        if(checkNullOrEmpty(bordercolor)) {
            throw new InvalidArgumentException("The given \"bordercolor\" value cannot be null or an empty string.", "Il " +
                    "valore fornito per l'opzione \"bordercolor\" non puo' essere null o una stringa vuota.");
        }

        this.bordercolor = bordercolor;
    }

    /**
     * Sets the "expansion" option's value.
     * @param expansion The given "expansion" option's value. This value must conform to the syntax required by FFmpeg.
     * @throws InvalidArgumentException If the given value is null or an empty string
     * @see <a href="https://ffmpeg.org/ffmpeg-filters.html#drawtext_005fexpansion">
     *     https://ffmpeg.org/ffmpeg-filters.html#drawtext_005fexpansion</a> for specifications on the required syntax
     */
    public void setExpansion(@NotNull String expansion) throws InvalidArgumentException {
        if(checkNullOrEmpty(expansion)) {
            throw new InvalidArgumentException("The given \"expansion\" value cannot be null or an empty string.", "Il " +
                    "valore fornito per l'opzione \"expansion\" non puo' essere null o una stringa.");
        }

        this.expansion = expansion;
    }

    /**
     * Sets the "basetime" option's value.
     * @param basetime The given "basetime" option's value. This value cannot be negative.
     * @throws InvalidArgumentException If the given value is negative
     */
    public void setBaseTime(int basetime) throws InvalidArgumentException {
        if(basetime < 0) {
            throw new InvalidArgumentException("The \"basetime\" value cannot be negative.", "Il valore dell'opzione " +
                    "\"basetime\" non puo' essere negativo.");
        }

        this.basetime = basetime;
    }

    /**
     * Sets the "fix_bounds" option's value.
     * @param fix_bounds The given "fix_bounds" option's value
     */
    public void setFixBounds(boolean fix_bounds) {
        this.fix_bounds = fix_bounds;
    }

    /**
     * Sets the "fontcolor" option's value.
     * @param fontcolor The given "fontcolor" option's value. This value must be one accepted by FFmpeg.
     * @throws InvalidArgumentException If the given value is null or an empty string
     * @see <a href="https://ffmpeg.org/ffmpeg-utils.html#color-syntax">https://ffmpeg.org/ffmpeg-utils.html#color-syntax</a>
     * for the list of accepted color values
     */
    public void setFontColor(@NotNull String fontcolor) throws InvalidArgumentException {
        if(checkNullOrEmpty(fontcolor)) {
            throw new InvalidArgumentException("The given \"fontcolor\" option's value cannot be null or an empty string.",
                    "Il valore dell'opzione \"fontcolor\" non puo' essere null o una stringa vuota.");
        }

        this.fontcolor = fontcolor;
    }

    /**
     * Sets the "fontcolor_expr" option's value. If the "fontcolor" option is already set, this value will override it.
     * @param fontcolor_expr The new "fontcolor_expr" option's value. This value must be one accepted by FFmpeg.
     * @throws InvalidArgumentException If the given value is null or an empty string
     * @see <a href="https://ffmpeg.org/ffmpeg-utils.html#color-syntax">https://ffmpeg.org/ffmpeg-utils.html#color-syntax</a>
     * for the list of accepted color values.
     */
    public void setFontColorExpr(@NotNull String fontcolor_expr) throws InvalidArgumentException {
        if(checkNullOrEmpty(fontcolor_expr)) {
            throw new InvalidArgumentException("The given \"fontcolor_expr\" option's value cannot be null or an empty " +
                    "string.", "Il valore dell'opzione \"fontcolor_expr\" non puo' essere null o una stringa vuota.");
        }

        this.fontcolor_expr = fontcolor_expr;
    }

    /**
     * Sets the "font" option's value.
     * @param font The new "font" option's value.
     * @throws InvalidArgumentException If the given value is null or an empty string
     */
    public void setFont(@NotNull String font) throws InvalidArgumentException {
        if(checkNullOrEmpty(font)) {
            throw new InvalidArgumentException("The given \"font\" value cannot be null or an empty string.", "Il valore " +
                    "dell'opzione \"font\" non puo' essere null o una stringa vuota.");
        }

        this.font = font;
    }

    /**
     * Sets the "fontfile" option's value.
     * @param fontfile The new "fontfile" option's value. This value must correspond to an existing and readable file.
     * @throws InvalidArgumentException If the given value is null or an empty string, or it does not correspond to an
     * existing or readable file
     */
    public void setFontFile(@NotNull String fontfile) throws InvalidArgumentException {
        if(checkNullOrEmpty(fontfile)) {
            throw new InvalidArgumentException("The given \"fontfile\" option's value cannot be null or an empty string.",
                    "Il valore dell'opzione \"fontfile\" non puo' essere null o una stringa vuota.");
        }

        Path fontpath = Paths.get(fontfile);
        if(!Files.exists(fontpath) || !Files.isReadable(fontpath)) {
            throw new InvalidArgumentException("The given \"fontfile\" option's value does not correspond to an existing " +
                    "or readable file.", "Il valore dell'opzione \"fontfile\" non corrisponde ad un file esistente o " +
                    "leggibile.");
        }

        this.fontfile = fontfile;
    }

    /**
     * Sets the "alpha" option's value.
     * @param alpha The new "alpha" option's value. This value must be between 0.0 and 1.0.
     * @throws InvalidArgumentException If the given value is less than 0.0 or greater than 1.0
     */
    public void setAlpha(double alpha) throws InvalidArgumentException {
        if(alpha < 0.0 || alpha > 1.0) {
            throw new InvalidArgumentException("The given \"alpha\" option's value must be between 0.0 and 1.0.", "Il " +
                    "valore dell'opzione \"alpha\" deve essere compreso tra 0.0 e 1.0.");
        }

        this.alpha = alpha;
    }

    /**
     * Sets the "fontsize" option's value.
     * @param fontsize The new "fontsize" option's value. This value must be greater than 0.
     * @throws InvalidArgumentException If the given value is less than or equal to zero
     */
    public void setFontSize(int fontsize) throws InvalidArgumentException {
        if(fontsize <= 0) {
            throw new InvalidArgumentException("The \"fontsize\" option's value cannot be less than or equal to zero.",
                    "Il valore dell'opzione \"fontsize\" non puo' essere minore o uguale a zero.");
        }

        this.fontsize = fontsize;
    }

    /**
     * Sets the "text_shaping" option's value.
     * @param val The new "text_shaping" option's value.
     */
    public void setTextShaping(int val) {
        text_shaping = val;
    }

    /**
     * Sets the "ft_load_flags" option's value.
     * @param ft_load_flags The new "ft_load_flags" option's value. This value cannot be null or an empty string, and it
     *                      must be recognizable by FFmpeg.
     * @throws InvalidArgumentException If the given value is null or an empty string, or if it is not recognizable by
     * FFmpeg
     */
    public void setFtLoadFlags(@NotNull String ft_load_flags) throws InvalidArgumentException {
        if(checkNullOrEmpty(ft_load_flags)) {
            throw new InvalidArgumentException("The given \"ft_load_flags\" option's value cannot be null or an empty " +
                    "string.", "Il valore dell'opzione \"ft_load_flags\" non puo' essere null o una stringa vuota.");
        }

        switch(ft_load_flags) {
            case "default", "no_scale", "no_hinting", "render", "no_bitmap", "vertical_layout", "force_autohint",
                 "crop_bitmap", "pedantic", "ignore_global_advance_width", "no_recurse", "ignore_transform", "monochrome",
                 "linear_design", "no_autohint" -> this.ft_load_flags = ft_load_flags;
            default -> throw new InvalidArgumentException("The given \"ft_load_flags\" option's value must be one recognized " +
                    "by FFmpeg.", "Il valore dell'opzione \"ft_load_flags\" deve essere riconoscibile da FFmpeg.");
        }
    }

    /**
     * Sets the "shadowcolor" option's value.
     * @param shadowcolor The new "shadowcolor" option's value. This value cannot be null or an empty string, and it must
     *                    be recognizable by FFmpeg.
     * @throws InvalidArgumentException If the given value is null or an empty string.
     * @see <a href="https://ffmpeg.org/ffmpeg-utils.html#color-syntax">https://ffmpeg.org/ffmpeg-utils.html#color-syntax</a>
     * for the list of accepted color names
     */
    public void setShadowColor(@NotNull String shadowcolor) throws InvalidArgumentException {
        if(checkNullOrEmpty(shadowcolor)) {
            throw new InvalidArgumentException("The given \"shadowcolor\" option's value cannot be null or an empty string.",
                    "Il valore dell'opzione \"shadowcolor\" non puo' essere null o una stringa vuota.");
        }

        this.shadowcolor = shadowcolor;
    }

    /**
     * Sets the "boxw" option's value.
     * @param boxw The new "boxw" option's value. This value must be greater than 0.
     * @throws InvalidArgumentException If the given value is less than or equal to zero
     */
    public void setBoxW(int boxw) throws InvalidArgumentException {
        if(boxw <= 0) {
            throw new InvalidArgumentException("The given \"boxw\" option's value cannot be less than or equal to zero.",
                    "Il valore dell'opzione \"boxw\" non puo' essere minore o uguale a zero.");
        }

        this.boxw = boxw;
    }

    /**
     * Sets the "boxh" option's value.
     * @param boxh The new "boxh" option's value. This value must be greater than 0.
     * @throws InvalidArgumentException If the given value is less than or equal to zero
     */
    public void setBoxH(int boxh) throws InvalidArgumentException {
        if(boxh <= 0) {
            throw new InvalidArgumentException("The given \"boxh\" option's value cannot be less than or equal to zero.",
                    "Il valore dell'opzione \"boxw\" non puo' essere minore o uguale a zero.");
        }

        this.boxh = boxh;
    }

    /**
     * Sets the "shadowx" option's value.
     * @param shadowx The given "shadowx" option's value.
     */
    public void setShadowX(int shadowx) {
        this.shadowx = shadowx;
    }

    /**
     * Sets the "shadowy" option's value.
     * @param shadowy The given "shadowy" option's value.
     */
    public void setShadowY(int shadowy) {
        this.shadowy = shadowy;
    }

    /**
     * Sets the "start_number" option's value.
     * @param start_number The new "start_number" option's value. This value must be greater than or equal to zero.
     * @throws InvalidArgumentException If the given value is less than zero
     */
    public void setStartNumber(int start_number) throws InvalidArgumentException {
        if(start_number < 0) {
            throw new InvalidArgumentException("The given \"start_number\" option's value cannot be negative.", "Il valore " +
                    "dell'opzione \"start_number\" non puo' essere negativo.");
        }

        this.start_number = start_number;
    }

    /**
     * Sets the "tabsize" option's value.
     * @param tabsize The new "tabsize" option's value. This value must be greater than or equal to zero.
     * @throws InvalidArgumentException If the given value is less than zero
     */
    public void setTabSize(int tabsize) throws InvalidArgumentException {
        if(tabsize < 0) {
            throw new InvalidArgumentException("The given \"tabsize\" option's value cannot be negative.", "Il valore " +
                    "dell'opzione \"tabsize\" non puo' essere negativo.");
        }

        this.tabsize = tabsize;
    }

    /**
     * Sets the "timecode" value.
     * @param timecode The new "timecode" value.
     */
    public void setTimeCode(TimeCode timecode) {
        this.timecode = Objects.requireNonNullElseGet(timecode, TimeCode::new);
    }

    /**
     * Sets the "tc24hmax" option's value.
     * @param tc24hmax The new "tc24hmax" option's value. This value must be either 0 or 1.
     * @throws InvalidArgumentException If the given value is different from 0 or 1
     */
    public void setTc24HMax(int tc24hmax) throws InvalidArgumentException {
        if(tc24hmax != 0 && tc24hmax != 1) {
            throw new InvalidArgumentException("The \"tc24hmax\" option's value must be either 0 or 1.", "Il valore " +
                    "dell'opzione \"tc24hmax\" deve essere pari a 0 o 1.");
        }

        this.tc24hmax = tc24hmax;
    }

    /**
     * Sets the "text" option's value.
     * @param text The given "text" option's value. This value cannot be null or an empty string.
     * @throws InvalidArgumentException If the "textfile" option is already set or the given value is null or an empty
     * string
     */
    public void setText(@NotNull String text) throws InvalidArgumentException {
        if(!checkNullOrEmpty(textfile)) {
            throw new InvalidArgumentException("The given \"text\" option's value cannot be set because the \"textfile\" " +
                    "option is already set.", "Il valore dell'opzione \"text\" non puo' essere impostato perche' quello " +
                    "dell'opzione \"textfile\" e' gie' stato impostato.");
        }
        if(text == null) {
            throw new InvalidArgumentException("The given \"text\" value cannot be null.", "Il valore dell'opzione " +
                    "\"text\" non puo' essere null.");
        }

        this.text = text;
    }

    /**
     * Sets the "textfile" option's value.
     * @param textfile The given "textfile" option's value. This value cannot be null.
     * @throws InvalidArgumentException If the "text" option is already set or the given value is null
     */
    public synchronized void setTextFile(@NotNull String textfile) throws InvalidArgumentException {
        if(!checkNullOrEmpty(text)) {
            throw new InvalidArgumentException("The given \"textfile\" option's value cannot be set because the \"text\" " +
                    "option is already set.", "Il valore dell'opzione \"textfile\" non puo' essere impostato perche' quello " +
                    "dell'opzione \"text\" e' gia' stato impostato.");
        }

        if(textfile == null) {
            throw new InvalidArgumentException("The given \"textfile\" value cannot be null.", "Il valore dell'opzione " +
                    "\"text\" non puo' essere null.");
        }

        this.textfile = textfile;
    }

    /**
     * Sets the "text_source" option's value.
     * @param text_source The new "text_source" option's value. This value must not be null.
     * @throws InvalidArgumentException If the given value is null
     */
    public void setTextSource(@NotNull String text_source) throws InvalidArgumentException {
        if(text_source == null) {
            throw new InvalidArgumentException("The given \"textsource\" option's value cannot be null.", "Il valore " +
                    "dell'opzione \"textsource\" non puo' essere null.");
        }

        this.text_source = text_source;
    }

    /**
     * Sets the "reload" option's value.
     * @param val The new "reload" option's value. This value cannot be negative or greater than INT_MAX.
     * @throws InvalidArgumentException If the given value is negative
     */
    public void setReload(int val) throws InvalidArgumentException {
        if (val < 0) {
            throw new InvalidArgumentException("The given \"reload\" option's value cannot be negative.", "Il valore " +
                    "dell'opzione \"reload\" non puo' essere negativo.");
        }

        this.reload = val;
    }

    /**
     * Sets the "x" option's value.
     * @param x The new "x" option's value. This value must be either a number greater than or equal to zero or a value
     *          recognizable by FFmpeg.
     * @throws InvalidArgumentException If the given value is null or an empty string or if it is a number and it is
     * negative
     */
    public void setX(@NotNull String x) throws InvalidArgumentException {
        if(checkNullOrEmpty(x)) {
            throw new InvalidArgumentException("The given \"x\" option's value cannot be null or an empty string.", "Il " +
                    "valore dell'opzione \"x\" non puo' essere null o una stringa vuota.");
        }

        try {
            int i = Integer.parseInt(x);
            if(i < 0) {
                throw new InvalidArgumentException("The given \"x\" option's value cannot be negatve.", "Il valore " +
                        "dell'opzione \"x\" non puo' essere negativo.");
            }
        } catch(NumberFormatException ignored) {
            //Nothing here...
        } finally {
            this.x = x;
        }
    }

    /**
     * Sets the "y" option's value.
     * @param y The new "y" option's value. This value must be either a number greater than or equal to zero or a value
     *          recognizable by FFmpeg.
     * @throws InvalidArgumentException If the given value is null or an empty string or if it is a number and it is
     * negative
     */
    public void setY(@NotNull String y) throws InvalidArgumentException {
        if(checkNullOrEmpty(y)) {
            throw new InvalidArgumentException("The given \"y\" option's value cannot be null or an empty string.", "Il " +
                    "valore dell'opzione \"y\" non puo' essere null o una stringa vuota.");
        }

        try {
            int i = Integer.parseInt(y);
            if(i < 0) {
                throw new InvalidArgumentException("The given \"y\" option's value cannot be negative.", "Il valore " +
                        "dell'opzione \"x\" non puo' essere negativo.");
            }
        } catch(NumberFormatException ignored) {
            //Nothing here...
        } finally {
            this.y = y;
        }
    }

    @Override
    public void updateMap() {
        options.put("box", String.valueOf(box));
        options.put("boxborderw", boxborderw.toString());
        options.put("boxcolor", boxcolor);
        options.put("line_spacing", String.valueOf(line_spacing));
        options.put("text_align", text_align);
        options.put("y_align", y_align);
        options.put("borderw", String.valueOf(borderw));
        options.put("bordercolor", bordercolor);
        options.put("expansion", expansion);
        options.put("basetime", String.valueOf(basetime));
        options.put("fix_bounds", String.valueOf(fix_bounds));
        options.put("fontcolor", fontcolor);
        options.put("fontcolor_expr", fontcolor_expr);
        options.put("font", font);
        options.put("fontfile", fontfile);
        options.put("alpha", String.valueOf(alpha));
        options.put("fontsize", String.valueOf(fontsize));
        options.put("text_shaping", String.valueOf(text_shaping));
        options.put("ft_load_flags", ft_load_flags);
        options.put("shadowcolor", shadowcolor);
        if(boxw != 0) {
            options.put("boxw", String.valueOf(boxw));
        }
        if(boxh != 0) {
            options.put("boxh", String.valueOf(boxh));
        }
        options.put("shadowx", String.valueOf(shadowx));
        options.put("shadowy", String.valueOf(shadowy));
        options.put("start_number", String.valueOf(start_number));
        options.put("tabsize", String.valueOf(tabsize));
        options.put("timecode", timecode.toString());
        options.put("timecode_rate", String.valueOf(timecode.getRate()));
        options.put("tc24hmax", String.valueOf(tc24hmax));
        options.put("text", text);
        options.put("textfile", textfile);
        if(!checkNullOrEmpty(text_source)) {
            options.put("text_source", text_source);
        }
        options.put("reload", String.valueOf(reload));
        options.put("x", x);
        options.put("y", y);
    }
}
