package it.disi.unitn;

import it.disi.unitn.exceptions.InvalidArgumentException;
import org.apache.commons.exec.*;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Convenience class to execute ffmpeg commands.
 */
public class FFMpeg {

    private final FFMpegBuilder ffBuilder;

    FFMpeg(@NotNull FFMpegBuilder ffBuilder) {
        this.ffBuilder = ffBuilder;
    }

    private void execProcess(boolean withTimeout, long timeout) throws IOException {
        String command = ffBuilder.getCommand();
        CommandLine cmdLine = CommandLine.parse(command);
        PumpStreamHandler streamHandler = new PumpStreamHandler();
        DefaultExecutor executor = DefaultExecutor.builder().get();
        ExecuteWatchdog watchdog = null;
        ExecuteWatchdog.Builder builder = ExecuteWatchdog.builder();
        builder.setTimeout(Duration.ofMillis(timeout));
        if(withTimeout) {
            watchdog = builder.get();
        }
        executor.setStreamHandler(streamHandler);
        streamHandler.start();
        if(withTimeout) {
            executor.setWatchdog(watchdog);
        }
        int exitCode = executor.execute(cmdLine);
        try {
            if(exitCode != 0) {
                System.err.println("EXIT CODE: " + exitCode);
                throw new Exception("An error has occurred.");
            }
        } catch(Exception ex) {
            //ex.printStackTrace();
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }

    /**
     * This method accomplishes the same goal as executeCMD(long, TimeUnit), but it does not set a time limit for the
     * current thread to wait for the child process's termination. Use with caution. This method should be used only for
     * those situations where the developer can be sure that the process will terminate in a reasonable time interval or
     * where waiting for the child process is not a problem.
     * @throws IOException If an I/O error occurs
     */
    public void executeCMD() throws IOException {
        //Utilizzo la libreria Apache Commons Exec per eseguire FFmpeg.
        execProcess(false, 0L);
    }

    /**
     * This method converts the given timeout in milliseconds using a given TimeUnit constant.
     * @param timeout The given timeout
     * @param tu The given TimeUnit instance or constant. This value defaults to TimeUnit.MILLISECONDS
     * @return The given timeout in milliseconds
     */
    private long timeConversion(long timeout, @NotNull TimeUnit tu) {
        switch(tu) {
            case DAYS -> {
                return timeout*86400000;
            }
            case HOURS -> {
                return timeout*3600000;
            }
            case MINUTES -> {
                return timeout*60000;
            }
            case SECONDS -> {
                return timeout*1000;
            }
            case MICROSECONDS -> {
                return (long)(0.001*timeout);
            }
            default -> {
                return timeout;
            }
        }
    }

    /**
     * This method executes the given command on the ProcessBuilder instance on which this class has been instantiated.
     * @param timeout The maximum time interval the calling process will wait for the child process to terminate
     * @param timeUnit The TimeUnit instance that specifies the time unit associated to the first parameter
     * @throws IOException If an I/O error occurs
     * @throws InvalidArgumentException If the given timeout is negative or the TimeUnit instance is null
     */
    public void executeCMD(long timeout, @NotNull TimeUnit timeUnit) throws IOException, InvalidArgumentException {
        if(timeout < 0) {
            throw new InvalidArgumentException("The given timeout cannot be negative.", "Il timeout fornito non puo' essere " +
                    "negativo.");
        }
        if(timeUnit == null) {
            throw new InvalidArgumentException("The given TimeUnit instance cannot be null.", "L'istanza di TimeUnit " +
                    "fornita non puo' essere null.");
        }
        /*ProcessBuilder builder;
        if(SystemUtils.IS_OS_WINDOWS) {
            builder = new ProcessBuilder(ffBuilder.getCommand());
        } else {
            builder = new ProcessBuilder("bash", "-c", ffBuilder.getCommand());
        }

        try {
            Process p = builder.start();
            builder.inheritIO();*/

            /*
             * FFMpeg hangs when we do not use two separate threads to handle the Error and Output streams.
             * See here: https://ffmpeg-user.ffmpeg.narkive.com/3WBzsW4a/ffmpeg-hangs-when-being-executed-from-within-java
             */
            /*InputHandler errorHandler = new InputHandler(p.getErrorStream(), "Error Stream");
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
            System.err.println(ex.getMessage());
        }*/
        
        //A questo punto, utilizzo la libreria Apache Commons Exec per eseguire FFmpeg.
        execProcess(true, timeConversion(timeout, timeUnit));
    }
}