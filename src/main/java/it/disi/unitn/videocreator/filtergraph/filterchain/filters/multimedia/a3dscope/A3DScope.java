package it.disi.unitn.videocreator.filtergraph.filterchain.filters.multimedia.a3dscope;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.size.Size;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements FFmpeg's "a3dscope" multimedia filter.
 */
public class A3DScope extends Filter {

    private int rate, fov, roll, pitch, yaw, xzoom, yzoom, zzoom, xpos, ypos, zpos, length;

    private Size size;

    /**
     * This class's constructor. Constructs a new filter (whether video or audio).
     *+
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    protected A3DScope() throws InvalidArgumentException {
        super("a3dscope");

        rate = 25;
        size = new Size();
        size.setSizeID("hd720");
        fov = 90;
        roll = 0;
        pitch = 0;
        yaw = 0;
        xzoom = 0;
        yzoom = 0;
        zzoom = 0;
        xpos = 0;
        ypos = 0;
        zpos = 0;
        length = 0;
    }

    /**
     * This method sets the frame rate for the filter.
     * @param rate The given frame rate. This value cannot be less than or equal to zero.
     * @throws InvalidArgumentException If the given frame rate is less than or equal to zero
     */
    public void setRate(int rate) throws InvalidArgumentException {
        if(rate <= 0) {
            throw new InvalidArgumentException("The frame rate cannot be less than or equal to zero.", "Il frame rate " +
                    "non puo' essere minore o uguale a zero.");
        }
        this.rate = rate;
    }

    /**
     * This method sets the size's ID.
     * @param sizeID The given size's ID. This value must be accepted by FFmpeg.
     * @throws InvalidArgumentException If the given value is null, an empty string, or it is not accepted by FFmpeg
     */
    public void setSizeID(@NotNull String sizeID) throws InvalidArgumentException {
        Size size = new Size();
        if(!size.checkSizeID(sizeID)) {
            throw new InvalidArgumentException("The size's ID must be accepted by FFmpeg.", "L'ID della dimensione deve " +
                    "essere accettato da FFmpeg.");
        }

        size.setSizeID(sizeID);
    }

    /**
     * This method sets the field of view.
     * @param fov The given field of view. This value must be between 40 and 150 (included).
     * @throws InvalidArgumentException If the given field of view is less than 40 or greater than 150
     */
    public void setFOV(int fov) throws InvalidArgumentException {
        if(fov < 40 || fov > 150) {
            throw new InvalidArgumentException("The field of view cannot be less than 40 or greater than 150.", "L'ampiezza " +
                    "del campo visivo non puo' essere minore di 40 o maggiore di 150.");
        }

        this.fov = fov;
    }

    /**
     * This method sets the camera roll's angle.
     * @param roll The given camera roll's angle. This value must be greater than or equal to zero.
     * @throws InvalidArgumentException If the camera roll's angle is less than zero
     */
    public void setCameraRoll(int roll) throws InvalidArgumentException {
        if(roll < 0) {
            throw new InvalidArgumentException("The roll camera angle cannot be less than 0.", "L'angolo di rotazione " +
                    "della camera non puo' essere minore di zero.");
        }

        this.roll = roll;
    }

    /**
     * This method sets the camera's pitch angle.
     * @param pitch The given pitch angle. This value must be greater than or equal to zero.
     * @throws InvalidArgumentException If the given camera pitch angle is less than zero
     */
    public void setCameraPitch(int pitch) throws InvalidArgumentException {
        if (pitch < 0) {
            throw new InvalidArgumentException("The camera's pitch cannot be less than 0.", "L'angolo di inclinazione " +
                    "della camera non puo' essere minore di zero.");
        }

        this.pitch = pitch;
    }

    /**
     * This method sets the camera's yaw angle.
     * @param yaw The given camera yaw angle. This value must be greater than or equal to zero.
     * @throws InvalidArgumentException If the given value is less than zero
     */
    public void setCameraYaw(int yaw) throws InvalidArgumentException {
        if(yaw < 0) {
            throw new InvalidArgumentException("The camera's yaw angle cannot be less than 0.", "L'angolo di imbardata " +
                    "della camera non puo' essere minore di zero.");
        }

        this.yaw = yaw;
    }

    /**
     * This method sets the "xzoom" parameter's value.
     * @param xzoom The given "xzoom" parameter's value. This value cannot be less than zero.
     * @throws InvalidArgumentException If the given value is less than zero
     */
    public void setXZoom(int xzoom) throws InvalidArgumentException {
        if(xzoom < 0) {
            throw new InvalidArgumentException("The zoom's x-value cannot be less than 0.", "Il valore dello zoom " +
                    "sull'asse delle ascisse non puo' essere minore di zero.");
        }

        this.xzoom = xzoom;
    }

    /**
     * This method sets the "yzoom" parameter's value.
     * @param yzoom The given "yzoom" parameter's value. This value cannot be less than zero.
     * @throws InvalidArgumentException If the given value is less than zero
     */
    public void setYZoom(int yzoom) throws InvalidArgumentException {
        if(yzoom < 0) {
            throw new InvalidArgumentException("The zoom's y-value cannot be less than 0.", "Il valore dello zoom " +
                    "sull'asse delle ordinate non puo' essere minore di zero.");
        }

        this.yzoom = yzoom;
    }

    /**
     * This method sets the "zzoom" parameter's value.
     * @param zzoom The given "zzoom" parameter's value. This value cannot be less than zero.
     * @throws InvalidArgumentException If the given value is less than zero
     */
    public void setZZoom(int zzoom) throws InvalidArgumentException {
        if(zzoom < 0) {
            throw new InvalidArgumentException("The zoom's z-value cannot be less than 0.", "Il valore della profondita' " +
                    "dello zoom non puo' essere minore di zero.");
        }

        this.zzoom = zzoom;
    }

    /**
     * This is a utility method to set the zoom values in all directions. See the documentation for {@code setXZoom()},
     * {@code setYZoom()} and {@code setZZoom()} for more details.
     * @param xzoom The xzoom value.
     * @param yzoom The yzoom value.
     * @param zzoom The zzoom value.
     * @throws InvalidArgumentException If any of the given values is less than zero
     */
    public void setZoom(int xzoom, int yzoom, int zzoom) throws InvalidArgumentException {
        setXZoom(xzoom);
        setYZoom(yzoom);
        setZZoom(zzoom);
    }

    /**
     * This method sets the "xpos" parameter's value.
     * @param xpos The "xpos" parameter's value. This value cannot be less than zero.
     * @throws InvalidArgumentException If the given value is less than zero
     */
    public void setXPos(int xpos) throws InvalidArgumentException {
        if(xpos < 0) {
            throw new InvalidArgumentException("The xpos value cannot be less than 0.", "Il valore del parametro xpos " +
                    "non puo' essere minore di zero.");
        }

        this.xpos = xpos;
    }

    /**
     * This method sets the "ypos" parameter's value.
     * @param ypos The "ypos" parameter's value. This value cannot be less than zero.
     * @throws InvalidArgumentException If the given value is less than zero
     */
    public void setYPos(int ypos) throws InvalidArgumentException {
        if(ypos < 0) {
            throw new InvalidArgumentException("The ypos value cannot be less than 0.", "Il valore del parametero ypos " +
                    "non puo' essere minore di zero.");
        }

        this.ypos = ypos;
    }

    /**
     * This method sets the "zpos" parameter's value.
     * @param zpos The "zpos" parameter's value. This value cannot be less than zero.
     * @throws InvalidArgumentException If the given value is less than zero
     */
    public void setZPos(int zpos) throws InvalidArgumentException {
        if(zpos < 0) {
            throw new InvalidArgumentException("The zpos value cannot be less than 0.", "Il valore del parametro zpos " +
                    "non puo' essere minore di zero.");
        }

        this.zpos = zpos;
    }

    /**
     * This method sets the position in all directions. See the documentation for {@code setXPos()}, {@code setYPos()}
     * and {@code setZPos()} for more details.
     * @param xpos The xpos value.
     * @param ypos The ypos value.
     * @param zpos The zpos value.
     * @throws InvalidArgumentException If any of the given values is less than zero
     */
    public void setPos(int xpos, int ypos, int zpos) throws InvalidArgumentException {
        setXPos(xpos);
        setYPos(ypos);
        setZPos(zpos);
    }

    /**
     * This method sets the displayed audio wave's length in number of frames.
     * @param length The given audio wave's length. This value cannot be less than or equal to zero.
     * @throws InvalidArgumentException If the given value is less than zero
     */
    public void setLength(int length) throws InvalidArgumentException {
        if (length <= 0) {
            throw new InvalidArgumentException("The length of the displayed audio waves cannot be less than or equal to " +
                    "zero.", "La lunghezza delle onde audio mostrate non puo' essere minore o uguale a zero.");
        }

        this.length = length;
    }

    @Override
    public void updateMap() {
        options.put("rate", String.valueOf(rate));
        options.put("size", size.getSizeID());
        options.put("fov", String.valueOf(fov));
        if(roll > 0) {
            options.put("roll", String.valueOf(roll));
        }
        if(pitch > 0) {
            options.put("pitch", String.valueOf(pitch));
        }
        if(yaw > 0) {
            options.put("yaw", String.valueOf(yaw));
        }
        if(xzoom > 0) {
            options.put("xzoom", String.valueOf(xzoom));
        }
        if(yzoom > 0) {
            options.put("yzoom", String.valueOf(yzoom));
        }
        if(zzoom > 0) {
            options.put("zzoom", String.valueOf(zzoom));
        }
        if(xpos > 0) {
            options.put("xpos", String.valueOf(xpos));
        }
        if(ypos > 0) {
            options.put("ypos", String.valueOf(ypos));
        }
        if(zpos > 0) {
            options.put("zpos", String.valueOf(zpos));
        }
        if(length > 0) {
            options.put("length", String.valueOf(length));
        }
    }
}
