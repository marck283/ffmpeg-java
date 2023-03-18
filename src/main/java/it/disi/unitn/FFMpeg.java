package it.disi.unitn;

import org.jetbrains.annotations.NotNull;

/**
 * Convenience class to execute ffmpeg commands.
 */
public class FFMpeg {

    private final FFMpegBuilder ffBuilder;
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
            //p = Runtime.getRuntime().exec(ffBuilder.getCommand());

            // wait for the process's termination before continuing
            p.waitFor();
        } catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("oops");
            return;
        }
    }
}