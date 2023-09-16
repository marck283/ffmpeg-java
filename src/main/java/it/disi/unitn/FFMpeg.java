package it.disi.unitn;

import it.disi.unitn.streamhandlers.InputHandler;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Convenience class to execute ffmpeg commands.
 */
public class FFMpeg {

    private final FFMpegBuilder ffBuilder;

    FFMpeg(@NotNull FFMpegBuilder ffBuilder) {
        this.ffBuilder = ffBuilder;
    }

    private void printStream(@NotNull InputStream istream) throws Exception {
        throw new Exception("An error has occurred.");
    }

    /**
     * This method accomplishes the same goal as executeCMD(long, TimeUnit), but it does not set a time limit for the
     * current thread to wait for the child process's termination. Use with caution. This method should be used only for
     * those situations where the developer can be sure that the process will terminate in a reasonable time interval or
     * where waiting for the child process is not a problem.
     */
    public void executeCMD() {
        ProcessBuilder builder = new ProcessBuilder(ffBuilder.getCommand());
        Process p;
        try {
            p = builder.start();
            InputStream istream = p.getErrorStream();

            /*
             * FFMpeg hangs when we do not use two separate threads to handle the Error and Output streams.
             * See here: https://ffmpeg-user.ffmpeg.narkive.com/3WBzsW4a/ffmpeg-hangs-when-being-executed-from-within-java
             */
            InputHandler errorHandler = new InputHandler(istream, "Error Stream");
            errorHandler.start();
            InputHandler inputHandler = new InputHandler(p.getInputStream(), "Output Stream");
            inputHandler.start();

            //Wait for the process's termination before continuing.
            int exitValue = p.waitFor();
            if(exitValue != 0) {
                p.destroy(); //Kill the process to release resources
                throw new Exception("An error has occurred.");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("oops");
        }
    }

    /**
     * This method executes the given command on the ProcessBuilder instance on which this class has been instantiated.
     * @param timeout The maximum time interval the calling process will wait for the child process to terminate
     * @param timeUnit The TimeUnit instance that specifies the time unit associated to the first parameter
     */
    public void executeCMD(long timeout, @NotNull TimeUnit timeUnit) {
        ProcessBuilder builder;
        if(SystemUtils.IS_OS_WINDOWS) {
            builder = new ProcessBuilder("?", ffBuilder.getCommand());
        } else {
            builder = new ProcessBuilder("bash", "-c", ffBuilder.getCommand());
        }

        try {
            Process p = builder.start();
            builder.inheritIO();

            /*
             * FFMpeg hangs when we do not use two separate threads to handle the Error and Output streams.
             * See here: https://ffmpeg-user.ffmpeg.narkive.com/3WBzsW4a/ffmpeg-hangs-when-being-executed-from-within-java
             */
            InputHandler errorHandler = new InputHandler(p.getErrorStream(), "Error Stream");
            errorHandler.start();
            InputHandler inputHandler = new InputHandler(p.getInputStream(), "Output Stream");
            inputHandler.start();

            //Wait for the process's termination or for the time limit to be reached before continuing.
            boolean exited = p.waitFor(timeout, timeUnit);
            if(!exited) {
                System.out.println("Exit code: " + p.exitValue());
                p.destroy(); //Kill the process to release resources
                throw new Exception("An error has occurred.");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }
}