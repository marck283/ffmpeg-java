package it.disi.unitn.json.processpool;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * This class is used to allow the execution of a specified callback at the process's termination.
 */
public class ProcessObserver extends Thread {
    private final Consumer<Process> callback;
    private final Process toExecute;

    private final int keepAliveTime;

    private final TimeUnit timeUnit;

    /**
     * Creates a new instance of this class.
     * @param c The callback to be executed at the process's termination
     * @param p The process to be observed
     * @param kat The keep-alive time
     * @param tu The TimeUnit for the keep-alive argument
     */
    public ProcessObserver(@NotNull Consumer<Process> c, @NotNull Process p, int kat, @NotNull TimeUnit tu) {
        super();
        callback = c;
        toExecute = p;
        keepAliveTime = kat;
        timeUnit = tu;
    }

    @Override
    public void run() {
        while(this.toExecute.isAlive()) {
            try {
                this.toExecute.waitFor(keepAliveTime, timeUnit);
            } catch (InterruptedException e) {
                this.toExecute.destroyForcibly();
                break;
            }
        }
        if(toExecute.exitValue() != 0) {
            Locale locale = Locale.getDefault();
            if(locale == Locale.ITALY || locale == Locale.ITALIAN) {
                System.err.println("Il processo di generazione dell'immagine e' terminato con un errore.");
            } else {
                System.err.println("The process that was created to generate the picture has terminated with an error.");
            }
            System.err.println(toExecute.exitValue());
            System.exit(toExecute.exitValue());
        }
        this.callback.accept(this.toExecute);
    }
}
