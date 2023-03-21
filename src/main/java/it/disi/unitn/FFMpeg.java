package it.disi.unitn;

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

    private void printStream(@NotNull Process p) throws Exception {
        InputStream istream = p.getErrorStream();
        byte[] byteArr = istream.readAllBytes();
        for(byte b: byteArr) {
            System.err.print((char)b);
        }
        throw new Exception("An error has occurred.");
    }

    /**
     * This method accomplishes the same goal as executeCMD(long, TimeUnit), but it does not set a time limit for the
     * current thread to wait for the child process's termination. Use with caution. This method should be used only for
     * those situations where the process terminates in a reasonable time interval or where waiting for the child process
     * is not a problem.
     */
    public void executeCMD() {
        ProcessBuilder builder = new ProcessBuilder(ffBuilder.getCommand());
        Process p;
        try {
            p = builder.start();

            //Wait for the process's termination before continuing.
            int exitValue = p.waitFor();
            if(exitValue != 0) {
                printStream(p);
            } else {
                //Why does the Thread instance not print to stdout?
                System.out.println("Video creation finished.");
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
        ProcessBuilder builder = new ProcessBuilder(ffBuilder.getCommand());
        Process p;
        try {
            p = builder.start();

            //Wait for the process's termination or for the time limit to be reached before continuing.
            boolean exited = p.waitFor(timeout, timeUnit);
            if(!exited) {
                printStream(p);
            } else {
                //Why does the Thread instance not print to stdout?
                System.out.println("Video creation finished.");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("oops");
        }
    }
}