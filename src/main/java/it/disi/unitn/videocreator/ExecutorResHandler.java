package it.disi.unitn.videocreator;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/**
 * This class handles the result of the command used to check either that the aspect ratio of each frame is compatible with
 * the given pixel format or that the given codec is supported by the current installation of FFmpeg.
 */
public class ExecutorResHandler implements ExecuteResultHandler {
    private int value = 0/*, width = -1, height = -1*/;

    private final OutputStream outstream;

    private final Path tempFile;

    /**
     * The class's constructor.
     * @param out The given OutputStream to write to.
     * @param tempp The Path instance corresponding to the OutputStream
     */
    public ExecutorResHandler(OutputStream out, Path tempp) {
        outstream = out;
        tempFile = tempp;
    }

    /**
     * The class's constructor to be used when width and height need to be specified.
     */
    public ExecutorResHandler() {
        outstream = OutputStream.nullOutputStream();
        tempFile = null;
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
     * @param val The value the field "value" will be set to.
     */
    public void setValue(int val) {
        value = val;
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
                setValue(1);
            }
            if(tempFile != null) {
                Files.delete(tempFile);
            }

            notifyAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onProcessFailed(ExecuteException e) {
        try {
            outstream.close();
            Locale l = Locale.getDefault();
            if(l == Locale.ITALY || l == Locale.ITALIAN) {
                System.err.println("Si e' verificato un errore.");
            } else {
                System.err.println("An error has occurred.");
            }
            System.exit(1);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
