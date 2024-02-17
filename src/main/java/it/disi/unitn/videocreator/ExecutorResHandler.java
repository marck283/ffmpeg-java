package it.disi.unitn.videocreator;

import it.disi.unitn.exceptions.InvalidArgumentException;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class handles the result of the command "ffmpeg -codecs -hide_banner".
 */
public class ExecutorResHandler implements ExecuteResultHandler {
    private int value = 0;

    private final OutputStream outstream;

    private final Path tempFile;

    private final String codecID;

    /**
     * The class's constructor.
     * @param out The given OutputStream to write to.
     * @param tempp The Path instance corresponding to the OutputStream
     * @param codecID The codec's ID
     * @throws InvalidArgumentException if any of the arguments to this constructor is null or an empty string
     */
    public ExecutorResHandler(@NotNull OutputStream out, @NotNull Path tempp, @NotNull String codecID)
            throws InvalidArgumentException {
        if(out == null || tempp == null || codecID == null || codecID.isEmpty()) {
            throw new InvalidArgumentException("No arguments to this constructor must be null or empty strings.");
        }
        outstream = out;
        tempFile = tempp;
        this.codecID = codecID;
    }

    /**
     * Gets the value parameter's value.
     * @return An integer equal to 1 if the requested string was found, otherwise 0.
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value equal to 1.
     */
    private void setValue() {
        value = 1;
    }

    /**
     * Suspends the current Thread. This method works just as wait().
     * @throws InterruptedException If the Thread is interrupted
     */
    public synchronized void doWait() throws InterruptedException {
        wait();
    }

    @Override
    public synchronized void onProcessComplete(int exitValue) {
        try {
            outstream.close();

            if(exitValue == 0) {
                setValue();
            }
            Files.delete(tempFile);

            notifyAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onProcessFailed(ExecuteException e) {
        try {
            outstream.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
