package it.disi.unitn;

import it.disi.unitn.videocreator.ExecutorResHandler;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * This class acts as a wrapper to control and execute the video creation processes.
 */
public class ProcessController {

    private final DefaultExecutor executor;

    private final ExecutorResHandler handler;

    /**
     * This class's constructor.
     * @param executor The DefaultExecutor instance
     * @param handler The ExecutorResHandler instance
     */
    public ProcessController(DefaultExecutor executor, ExecutorResHandler handler) {
        this.executor = executor;
        this.handler = handler;
    }

    /**
     * This method executes the video creation processes.
     * @param cmdline The given CommandLine instance representing the desired FFmpeg command
     * @return 1 if all processes complete successfully, otherwise 0
     * @throws IOException If an I/O error occurs
     * @throws InterruptedException If any Thread that guides the video creation process is interrupted.
     */
    public int execute(@NotNull CommandLine cmdline) throws IOException, InterruptedException {
        executor.execute(cmdline, handler);
        Thread t1 = new Thread(() -> {
            try {
                handler.doWait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t1.start();
        t1.join();

        int val = handler.getValue();
        handler.setValue(0);

        return val;
    }

}
