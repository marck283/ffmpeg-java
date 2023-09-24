package it.disi.unitn.json.processpool;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.json.JSONToImage;
import it.disi.unitn.streamhandlers.InputHandler;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * This class is used to create a thread pool which will be used to execute a specified number of threads.
 */
public class ProcessPool {
    //private final ThreadPoolExecutor executor;

    private final String scriptpath, imageExtension, pathToImagesFolder;

    private String desc;

    private final int width, height;

    private int index;

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
     * @throws IllegalArgumentException if the number of threads to be executed is greater than 1024
     */
    public ProcessPool(/*int num, int keepAliveTime, TimeUnit tu*/@NotNull File model, @NotNull String imageExtension,
                                                                  @NotNull String pathToImagesFolder, int width, int height) {
        /*if(num > 1024) {
            throw new IllegalArgumentException("Cannot execute more than 1024 threads.");
        }
        executor = new ThreadPoolExecutor(num, 1024, keepAliveTime, tu, new LinkedBlockingQueue<>());*/
        if(model == null || imageExtension == null || imageExtension.isEmpty() || pathToImagesFolder == null ||
                pathToImagesFolder.isEmpty()) {
            throw new IllegalArgumentException("No argument to this constructor can be null.");
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
     */
    public void setDesc(@NotNull String newdesc) {
        if(newdesc == null || newdesc.isEmpty()) {
            throw new IllegalArgumentException("The argument to this method cannot be null or an empty string.");
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
     * This method executes a process from a ProcessBuilder instance and keeps track of a callback to be executed at the
     * process's termination.
     //* @param pb The ProcessBuilder instance. This parameter must not be null
     //* @param s The command to be executed on the command line
     //* @param c The callback to be executed at the process's termination. This parameter must not be null
     * @param array The JsonArray instance from which to take the necessary information about the final pictures
     * @param jti The JSONToImage instance to be used
     * @throws IllegalArgumentException if any of the arguments to this method is null
     * @throws IOException if an I/O error occurs
     */
    public void execute(/*@NotNull ProcessBuilder pb String s, @NotNull Consumer<Process> c, */@NotNull JsonArray array,
                                                                                               @NotNull JSONToImage jti)
            throws IOException {
        /*Process p = pb.start();

        InputStream istream = p.getErrorStream();

        InputHandler errorHandler = new InputHandler(istream, "Error Stream");
        errorHandler.start();
        InputHandler inputHandler = new InputHandler(p.getInputStream(), "Output Stream");
        inputHandler.start();*/

        String s = "python3 " + scriptpath + " \"" + desc + "\" " + index + " " + imageExtension + " " + pathToImagesFolder +
                " " + width + " " + height;
        CommandLine cmdLine = CommandLine.parse(s);
        PumpStreamHandler streamHandler = new PumpStreamHandler();
        DefaultExecutor executor = new DefaultExecutor();
        ExecuteWatchdog watchdog = new ExecuteWatchdog(1800000); //30 minuti
        executor.setStreamHandler(streamHandler);
        executor.setWatchdog(watchdog);
        int exitCode = executor.execute(cmdLine);
        if(exitCode == 0) {
            try {
                StringExt i = new StringExt(String.valueOf(index));
                i.padStart();
                JsonObject obj = array.get(index).getAsJsonObject();
                String mime = obj.get("mime").getAsString();
                Path path = Paths.get(pathToImagesFolder, i.getVal() + "." + imageExtension);
                File image = path.toFile();
                byte[] arr = Files.readAllBytes(image.toPath());
                Files.write(path, arr);

                jti.modifyImage(obj, index, pathToImagesFolder, mime);
            } catch(InvalidArgumentException | IOException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                System.err.println("EXIT CODE: " + exitCode);
                throw new Exception("An error has occurred.");
            } catch(Exception ex) {
                ex.printStackTrace();
                System.err.println(ex.getMessage());
                System.exit(1);
            }
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
