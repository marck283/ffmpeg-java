package it.disi.unitn;

import it.disi.unitn.exceptions.InvalidArgumentException;
import org.apache.commons.exec.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Convenience class to execute ffmpeg commands.
 */
public class FFMpeg {

    private final FFMpegBuilder ffBuilder;

    private DefaultExecutor executor;

    FFMpeg(@NotNull FFMpegBuilder ffBuilder) {
        this.ffBuilder = ffBuilder;
    }

    private CommandLine setCommandLine(boolean withTimeout, long timeout, @NotNull String workdir) {
        String command = ffBuilder.getCommand();
        CommandLine cmdLine = CommandLine.parse(command);
        PumpStreamHandler streamHandler = new PumpStreamHandler();

        DefaultExecutor.Builder<DaemonExecutor.Builder> execBuilder = new DefaultExecutor.Builder<>();
        execBuilder.setWorkingDirectory(new File(workdir));

        executor = execBuilder.get();
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
        return cmdLine;
    }

    private void execProcess(boolean withTimeout, long timeout, @NotNull String workdir) throws IOException {
        CommandLine cmdLine = setCommandLine(withTimeout, timeout, workdir);
        int exitCode = executor.execute(cmdLine);
        try {
            if(exitCode != 0) {
                System.err.println("EXIT CODE: " + exitCode);
                throw new Exception("An error has occurred.");
            }
        } catch(Exception ex) {
            //ex.printStackTrace();
            System.err.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * This method executes a process given an instance of ExecuteResultHandler
     * @param withTimeout A boolean parameter to tell if there is going to be a timeout
     * @param timeout The given timeout
     * @param workdir The working directory
     * @param resHandler The ExecuteResultHandler instance
     * @throws IOException If an I/O error occurs
     */
    public void execProcessWithResHandler(boolean withTimeout, long timeout, @NotNull String workdir,
                                          @NotNull ExecuteResultHandler resHandler) throws IOException {
        CommandLine cmdLine = setCommandLine(withTimeout, timeout, workdir);
        executor.execute(cmdLine, resHandler);
    }

    /**
     * This method accomplishes the same goal as executeCMD(long, TimeUnit), but it does not set a time limit for the
     * current thread to wait for the child process's termination. Use with caution. This method should be used only for
     * those situations where the developer can be sure that the process will terminate in a reasonable time interval or
     * where waiting for the child process is not a problem.
     * @param workdir The working directory
     * @throws IOException If an I/O error occurs
     */
    public void executeCMD(@NotNull String workdir) throws IOException {
        //Utilizzo la libreria Apache Commons Exec per eseguire FFmpeg.
        execProcess(false, 0L, workdir);
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
                //TimeUnit.MILLISECONDS
                return timeout;
            }
        }
    }

    /**
     * This method executes the given command on the ProcessBuilder instance on which this class has been instantiated.
     * @param timeout The maximum time interval the calling process will wait for the child process to terminate.
     * @param timeUnit The TimeUnit instance that specifies the time unit associated to the first parameter.
     * @param workdir The chosen working directory.
     * @param resHandler The given ExecuteResultHandler instance. This value can be null.
     * @throws IOException If an I/O error occurs
     * @throws InvalidArgumentException If the given timeout is negative or the TimeUnit instance is null
     */
    public void executeCMD(long timeout, @NotNull TimeUnit timeUnit, @NotNull String workdir,
                           @Nullable ExecuteResultHandler resHandler) throws IOException, InvalidArgumentException {
        if(timeout < 0) {
            throw new InvalidArgumentException("The given timeout cannot be negative.", "Il timeout fornito non puo' essere " +
                    "negativo.");
        }
        if(timeUnit == null || StringExt.checkNullOrEmpty(workdir)) {
            throw new InvalidArgumentException("The time unit and the working directory cannot be null or empty strings.",
                    "L'unita' di tempo e la directory di lavoro non possono essere null o una stringa vuota.");
        }
        
        //A questo punto, utilizzo la libreria Apache Commons Exec per eseguire FFmpeg.
        if(resHandler == null) {
            execProcess(true, timeConversion(timeout, timeUnit), workdir);
        } else {
            execProcessWithResHandler(true, timeConversion(timeout, timeUnit), workdir, resHandler);
        }
    }
}