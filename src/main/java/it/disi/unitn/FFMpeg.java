package it.disi.unitn;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

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
    public void executeCMD() {
        ProcessBuilder builder = new ProcessBuilder(ffBuilder.getCommand());
        Process p;
        try {
            p = builder.start();

            // wait for the process's termination before continuing
            int exitValue = p.waitFor();
            if(exitValue != 0) {
                System.err.println(exitValue);
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