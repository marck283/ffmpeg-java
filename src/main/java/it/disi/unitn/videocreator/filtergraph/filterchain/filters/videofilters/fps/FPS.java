package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.fps;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements FFmpeg's "fps" video filter.
 */
public class FPS extends VideoFilter {

    private String fps, round, eof_action;

    private int start_time;

    /**
     * This class's constructor. Constructs a new video filter.
     */
    public FPS() {
        super("fps");
        fps = "pal";
        start_time = 0;
        round = "near";
        eof_action = "round";
    }

    /**
     * This method sets the "fps" parameter's value.
     * @param fps The given "fps" parameter's expression. THis expression must be recognized by FFmpeg.
     * @throws InvalidArgumentException If the given expression is not valid
     */
    public void setFPS(@NotNull String fps) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(fps)) {
            throw new InvalidArgumentException("The given \"fps\" expression cannot be null or an empty string.",
                    "L'espressione fornita per il parametro \"fps\" non puo' essere null o una stringa vuota.");
        }

        if(!fps.contains("source_fps") && !fps.contains("ntsc") && !fps.contains("pal") && !fps.contains("film") &&
        !fps.contains("ntsc_film")) {
            throw new InvalidArgumentException("The given \"fps\" expression must contain at least one of \"source_fps\", " +
                    "\"ntsc\", \"pal\", \"film\" or \"ntsc_film\".", "L'espressione fornita per il parametro \"fps\" " +
                    "deve contenere almeno un valore tra \"source_fps\", \"ntsc\", \"pal\", \"film\" or \"ntsc_film\".");
        }

        this.fps = fps;
    }

    /**
     * This method sets the "start_time" parameter's value.
     * @param start_time The given "start_time" value.
     */
    public void setStartTime(int start_time) {
        this.start_time = start_time;
    }

    /**
     * This method sets the "round" parameter's value.
     * @param round The given "round" parameter's value. This value cannot be null or an empty string, and it must be
     *              allowed by FFmpeg.
     * @throws InvalidArgumentException If the given value is null, an empty string, or it is not allowed by FFmpeg
     */
    public void setRound(@NotNull String round) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(round)) {
            throw new InvalidArgumentException("The given \"round\" value cannot be null or an empty string.", "Il valore " +
                    "fornito per il parametro \"round\" non puo' essere null o una stringa vuota.");
        }

        switch(round) {
            case "zero", "inf", "down", "up", "near" -> this.round = round;
            default -> throw new InvalidArgumentException("The given \"round\" value must be \"zero\", \"inf\", " +
                    "\"down\", \"up\", or \"near\".", "Il valore fornito per il parametro \"round\" deve essere pari a " +
                    "\"zero\", \"inf\", \"down\", \"up\" o \"near\".");
        }
    }

    /**
     * This method sets the "eof_action" parameter's value.
     * @param eof_action The given "eof_action" paramter's value. This value cannot be null or an empty string, and it
     *                   must be allowed by FFmpeg.
     * @throws InvalidArgumentException If the given value is null, an empty string, or it is not allowed by FFmpeg
     */
    public void setEOFAction(@NotNull String eof_action) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(eof_action)) {
            throw new InvalidArgumentException("The given \"eof_action\" value cannot be null or an empty string.",
                    "Il valore fornito per \"eof_action\" non puo' essere null o una stringa vuota.");
        }

        if(!eof_action.equals("round") && !eof_action.equals("pass")) {
            throw new InvalidArgumentException("The given \"eof_action\" value must be either \"round\" or \"pass\".",
                    "Il valore fornito per il parametro \"eof_action\" deve essere \"round\" o \"pass\".");
        }

        this.eof_action = eof_action;
    }

    @Override
    public void updateMap() {
        options.put("fps", fps);
        options.put("start_time", String.valueOf(start_time));
        options.put("round", round);
        options.put("eof_action", eof_action);
    }
}
