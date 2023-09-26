package it.disi.unitn.json.processpool;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.json.JSONToImage;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class handles the execution of a callback to be executed at the termination of any picture's inference.
 */
public class ExecutorResHandler implements ExecuteResultHandler {

    private final JsonArray array;

    private final int index;

    private final String pathToImagesFolder, imageExtension;

    private final JSONToImage jti;

    private final ProcessPool pp;

    /**
     * The class's constructor.
     * @param arr The JsonArray instance from which to extract all the necessary information
     * @param i The index after which the picture is to be named
     * @param pathToImagesFolder The path to each picture's destination folder
     * @param imageExtension The picture's file extension
     * @param jti The JSONToImage instance to be used
     * @param pp The ProcessPool instance to be used
     */
    public ExecutorResHandler(@NotNull JsonArray arr, int i, @NotNull String pathToImagesFolder,
                              @NotNull String imageExtension, @NotNull JSONToImage jti, @NotNull ProcessPool pp) {
        if (arr == null || pathToImagesFolder == null || pathToImagesFolder.isEmpty() || imageExtension == null ||
                imageExtension.isEmpty() || jti == null) {
            throw new IllegalArgumentException("No argument to this constructor can be null or an empty string.");
        }
        array = arr;
        index = i;
        this.pathToImagesFolder = pathToImagesFolder;
        this.imageExtension = imageExtension;
        this.jti = jti;
        this.pp = pp;
    }

    /**
     * To be executed on process completion
     *
     * @param exitValue the exit value of the sub-process
     */
    @Override
    public void onProcessComplete(int exitValue) {
        /*if(exitValue == 0) {
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
                        System.err.println("EXIT CODE: " + exitValue);
                        throw new Exception("An error has occurred.");
                    } catch(Exception ex) {
                        ex.printStackTrace();
                        System.err.println(ex.getMessage());
                        System.exit(1);
                    }
                }*/
        Thread t2 = new Thread(() -> {
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
                pp.removeHandler(this);
            } catch (InvalidArgumentException | IOException ex) {
                ex.printStackTrace();
                System.err.println(ex.getMessage());
                System.exit(1);
            }
        });
        t2.start();
        try {
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * To be executed on process failure.
     *
     * @param e the {@code ExecuteException} containing the root cause
     */
    @Override
    public void onProcessFailed(@NotNull ExecuteException e) {
        e.printStackTrace();
        System.err.println(e.getMessage());
        System.exit(1);
    }
}
