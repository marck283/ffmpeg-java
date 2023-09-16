package it.disi.unitn.json.processpool;

import it.disi.unitn.streamhandlers.InputHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * This class is used to create a thread pool which will be used to execute a specified number of threads.
 */
public class ProcessPool {
    private final ThreadPoolExecutor executor;

    /**
     * This constructor creates a new thread pool which will be used to execute a specified number of threads.
     * @param num The number of threads to be executed
     * @param keepAliveTime when the number of threads is greater than the core, this is the maximum time that excess
     *                      idle threads will wait for new tasks before terminating
     * @param tu the time unit for the keepAliveTime argument
     * @throws IllegalArgumentException if the number of threads to be executed is greater than 1024
     */
    public ProcessPool(int num, int keepAliveTime, TimeUnit tu) {
        if(num > 1024) {
            throw new IllegalArgumentException("Cannot execute more than 1024 threads.");
        }
        executor = new ThreadPoolExecutor(num, 1024, keepAliveTime, tu, new LinkedBlockingQueue<>());
    }

    /**
     * This method executes a process from a ProcessBuilder instance and keeps track of a callback to be executed at the
     * process's termination.
     * @param pb The ProcessBuilder instance. This parameter must not be null
     * @param c The callback to be executed at the process's termination. This parameter must not be null
     * @throws IllegalArgumentException if any of the arguments to this method is null
     * @throws IOException if an I/O error occurs
     */
    public void execute(@NotNull ProcessBuilder pb, @NotNull Consumer<Process> c) throws IOException {
        if(pb == null || c == null) {
            throw new IllegalArgumentException("Both arguments to this method must not be null.");
        }
        Process p = pb.start();

        InputStream istream = p.getErrorStream();

        InputHandler errorHandler = new InputHandler(istream, "Error Stream");
        errorHandler.start();
        InputHandler inputHandler = new InputHandler(p.getInputStream(), "Output Stream");
        inputHandler.start();

        executor.execute(new ProcessObserver(c, p, 30, TimeUnit.MINUTES));
    }

    /**
     * This method can be used to shut down the thread pool.
     * @return true if the thread pool is terminated, false otherwise
     */
    public boolean shutdown() {
        executor.shutdown();

        boolean aterm;
        try {
            aterm = executor.awaitTermination(30, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return aterm && executor.isTerminated();
    }
}
