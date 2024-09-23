package it.disi.unitn.transitions.rotation.processpool;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.json.JSONToImage;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class handles the execution of a callback to be executed at the termination of any picture's inference.
 */
public class ExecutorResHandler implements ExecuteResultHandler {

    private final ProcessPool pp;

    /**
     * The class's constructor.
     * @param pp The ProcessPool instance to be used
     * @throws InvalidArgumentException If any of the arguments given to this constructor is null or an empty string
     */
    public ExecutorResHandler(@NotNull ProcessPool pp)
            throws InvalidArgumentException {
        if (pp == null) {
            throw new InvalidArgumentException("No argument to this constructor can be null or an empty string.",
                    "Nessuno degli argomenti forniti a questo costruttore puo' essere null o una stringa vuota.");
        }
        this.pp = pp;
    }

    /**
     * To be executed on process completion.
     *
     * @param exitValue the exit value of the sub-process
     */
    @Override
    public void onProcessComplete(int exitValue) {
        pp.removeHandler(this);
    }

    /**
     * To be executed on process failure.
     *
     * @param e the {@code ExecuteException} containing the root cause
     */
    @Override
    public void onProcessFailed(@NotNull ExecuteException e) {
        //e.printStackTrace();
        System.err.println(e.getMessage());
        System.exit(1);
    }
}
