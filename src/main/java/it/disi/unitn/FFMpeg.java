package it.disi.unitn;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

/**
 * Convenience class to execute ffmpeg commands.
 */
public class FFMpeg {

    private FFMpegBuilder ffBuilder;
    private ProcessBuilder builder;

    FFMpeg(@NotNull FFMpegBuilder ffBuilder) {
        this.ffBuilder = ffBuilder;
    }

    /**
     * Executes the given command on the given ProcessBuilder instance.
     */
    public void executeCMD() {
        builder = new ProcessBuilder(ffBuilder.getCommand());

        builder.redirectErrorStream(true);
        Process p;
        try {
            p = builder.start();

            // wait for 500 milliseconds before continuing
            p.waitFor(500, TimeUnit.MILLISECONDS);
        } catch(FileNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("NOT FOUND");
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("oops");
            return;
        }
    }
}