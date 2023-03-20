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

    /**
     * Executes the given command on the given ProcessBuilder instance.
     */
    public void executeCMD(long timeout, TimeUnit timeUnit) {
        ProcessBuilder builder = new ProcessBuilder(ffBuilder.getCommand());
        Process p;
        try {
            p = builder.start();

            // wait for the process's termination before continuing
            boolean exited = p.waitFor(timeout, timeUnit);
            if(exited) {
                InputStream istream = p.getErrorStream();
                byte[] byteArr = istream.readAllBytes();
                for(byte b: byteArr) {
                    System.err.print((char)b);
                }
                throw new Exception("An error has occurred.");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("oops");
        }
    }
}