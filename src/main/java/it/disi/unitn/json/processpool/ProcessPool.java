package it.disi.unitn.json.processpool;

import com.google.gson.JsonArray;
/*import com.google.gson.JsonObject;
import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;*/
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.json.JSONToImage;
import org.apache.commons.exec.*;
//import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.owasp.encoder.Encode;

import java.io.File;
import java.io.IOException;
/*import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;*/
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to create a thread pool which will be used to execute a specified number of threads.
 */
public class ProcessPool {

    //private final ThreadPoolExecutor executor;

    private final String scriptpath, imageExtension, pathToImagesFolder;

    private String desc;

    private final int width, height;

    private int index;

    private final List<ExecutorResHandler> exlist = new ArrayList<>();

    /**
     * This constructor creates a new thread pool which will be used to execute a specified number of threads.
     //* @param num The number of threads to be executed
     //* @param keepAliveTime when the number of threads is greater than the core, this is the maximum time that excess
     //*                      idle threads will wait for new tasks before terminating
     //* @param tu the time unit for the keepAliveTime argument
     * @param model The File instance from which to create the process pool
     * @param imageExtension The picture's extension
     * @param pathToImagesFolder The path to the pictures' folder
     * @param width The pictures' width
     * @param height The pictures' height
     * @throws InvalidArgumentException If any of the arguments given to this constructor is null or less than or equal
     * to zero
     */
    public ProcessPool(@NotNull File model, @NotNull String imageExtension, @NotNull String pathToImagesFolder, int width,
                       int height) throws InvalidArgumentException {
        if(model == null || imageExtension == null || imageExtension.isEmpty() || pathToImagesFolder == null ||
                pathToImagesFolder.isEmpty() || width <= 0 || height <= 0) {
            throw new InvalidArgumentException("No argument to this constructor can be null, an empty string  or less " +
                    "than or equal to zero.", "Nessuno degli argomenti forniti a questo costruttore puo' essere null, " +
                    "una stringa vuota o minore o uguale a zero.");
        }
        scriptpath = model.getAbsolutePath();
        this.imageExtension = imageExtension;
        this.pathToImagesFolder = pathToImagesFolder;
        this.width = width;
        this.height = height;
    }

    /**
     * This method sets the description to be used when producing the final pictures.
     * @param newdesc The description to be used
     * @throws InvalidArgumentException If the argument given to this method is null or an empty string
     */
    public void setDesc(@NotNull String newdesc) throws InvalidArgumentException {
        if(newdesc == null || newdesc.isEmpty()) {
            throw new InvalidArgumentException("The argument to this method cannot be null or an empty string.", "L'argomento " +
                    "fornito a questo metodo non puo' essere null o una stringa vuota.");
        }
        desc = newdesc;
    }

    /**
     * This method sets the index to be used when producing the final pictures.
     * @param newindex The index to be used
     */
    public void setIndex(int newindex) {
        index = newindex;
    }

    /**
     * This method waits until all tasks have terminated correctly.
     * @throws InterruptedException if any thread interrupted the current thread before or while the current thread was
     * waiting. The interrupted status of the current thread is cleared when this exception is thrown.
     */
    public synchronized void doWait() throws InterruptedException {
        wait();
    }

    /**
     * This method removes ExecutorResHandler instances from the list of currently active instances.
     * @param handler The ExecutorResHandler instance to be used
     */
    public synchronized void removeHandler(ExecutorResHandler handler) {
        if(exlist.size() == 1 && exlist.contains(handler)) {
            //Notifies all threads that were waiting on this object's monitor. This call needs to happen either inside a
            //synchronized method or inside a synchronized code block.
            notifyAll();
        }
        exlist.remove(handler);
    }

    /**
     * This method executes a process from a ProcessBuilder instance and keeps track of a callback to be executed at the
     * process's termination.
     * @param array The JsonArray instance from which to take the necessary information about the final pictures
     * @param jti The JSONToImage instance to be used
     * @param timeout The timeout in milliseconds
     * @throws InvalidArgumentException if any of the arguments to this method is null
     */
    public void execute(@NotNull JsonArray array, @NotNull JSONToImage jti, long timeout) throws InvalidArgumentException {
        /*Process p = pb.start();

        InputStream istream = p.getErrorStream();

        InputHandler errorHandler = new InputHandler(istream, "Error Stream");
        errorHandler.start();
        InputHandler inputHandler = new InputHandler(p.getInputStream(), "Output Stream");
        inputHandler.start();*/

        try {
            /*String s = "python3 " + scriptpath + " \"" + desc + "\" " + index + " " + imageExtension + " " +
                    pathToImagesFolder + " " + width + " " + height;
            CommandLine cmdLine = CommandLine.parse(s);*/
            Map<String, String> m = new HashMap<>();

            String nscriptpath = Encode.forJava(scriptpath), ndesc = Encode.forJava(desc), niext = Encode.forJava(imageExtension),
            nptif = Encode.forJava(pathToImagesFolder);
            m.put("scriptpath", nscriptpath);
            m.put("desc", "\"" + ndesc + "\"");

            String i = String.valueOf(index);
            m.put(i, i);
            m.put("imageExtension", niext);
            m.put("pathToImagesFolder", nptif);

            String w = String.valueOf(width), h = String.valueOf(height);
            m.put("w", w);
            m.put("h", h);
            CommandLine cmdLine = CommandLine.parse("python3 ${scriptpath} ${desc} ${i} ${imageExtension} " +
                    "${pathToImagesFolder} ${w} ${h}", m);
            PumpStreamHandler streamHandler = new PumpStreamHandler();
            DefaultExecutor executor = DefaultExecutor.builder().get();
            ExecuteWatchdog.Builder builder = ExecuteWatchdog.builder();
            builder.setTimeout(Duration.ofMillis(timeout)); //Durata in minuti
            ExecuteWatchdog watchdog = builder.get();
            executor.setStreamHandler(streamHandler);
            executor.setWatchdog(watchdog);
            ExecutorResHandler exrhandler = new ExecutorResHandler(array, index, nptif, niext, jti, this);
            exlist.add(exrhandler);
            executor.execute(cmdLine, exrhandler);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getLocalizedMessage());
            System.exit(1);
        }

        //executor.execute(new ProcessObserver(c, p, 30, TimeUnit.MINUTES));
    }

    ///**
     //* This method can be used to shut down the thread pool.
     //* @return true if the thread pool is terminated, false otherwise
     //*/
    /*public boolean shutdown() {
        executor.shutdown();

        boolean aterm;
        try {
            aterm = executor.awaitTermination(30, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return aterm && executor.isTerminated();
    }*/
}
