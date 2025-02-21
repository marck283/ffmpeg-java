package it.disi.unitn.json.processpool;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.json.JSONToImage;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class handles the execution of a callback to be executed at the termination of any picture's inference.
 */
public class ExecutorResHandler implements ExecuteResultHandler {

    private final @NotNull JsonArray array;

    private final int index;

    private int param;

    private final @NotNull String pathToImagesFolder, imageExtension;

    private final @NotNull JSONToImage jti;

    /**
     * The class's constructor.
     * @param arr The JsonArray instance from which to extract all the necessary information
     * @param i The index after which the picture is to be named
     * @param pathToImagesFolder The path to each picture's destination folder
     * @param imageExtension The picture's file extension
     * @param jti The JSONToImage instance to be used
     */
    public ExecutorResHandler(@NotNull JsonArray arr, int i, @NotNull String pathToImagesFolder,
                              @NotNull String imageExtension, @NotNull JSONToImage jti) {
        if (arr == null || StringExt.checkNullOrEmpty(pathToImagesFolder) || StringExt.checkNullOrEmpty(imageExtension)
                || jti == null) {
            System.err.println((new InvalidArgumentException("No argument to this constructor can be null or an empty string.",
                    "Nessuno degli argomenti forniti a questo costruttore puo' essere null o una stringa vuota.")).getMessage());
            array = new JsonArray();
            index = -1;
            this.pathToImagesFolder = "";
            this.imageExtension = "";
            this.jti = jti;
            return;
        }
        array = arr;
        index = i;
        this.pathToImagesFolder = pathToImagesFolder;
        this.imageExtension = imageExtension;
        this.jti = jti;
        param = -1;
    }

    /**
     * Returns the value of a flag that tells if the process was successfully completed or not.
     * @return The value of a flag that tells if the process was successfully completed or not.
     */
    public int getParam() {
        return param;
    }

    /**
     * This method resolves the path to the given directories and joins them to form a single path.
     * @param first The first folder of the path
     * @param path Other folders of the path. These must be given in the same order as they appear
     *             in the file system (e.g., "foo" and "bar" must correspond to "foo/bar"
     *             in the file system)
     * @return The resulting Path instance
     */
    private @NotNull Path getPath(@NonNls String first, @NonNls String ... path) {
        Path p = Paths.get(first, path);
        if(p == null) {
            throw new RuntimeException("Invalid path.");
        }
        return p;
    }

    /**
     * To be executed on process completion.
     *
     * @param exitValue the exit value of the sub-process
     */
    @Override
    public void onProcessComplete(int exitValue) {
        Thread t2 = new Thread(() -> {
            try {
                StringExt i = new StringExt(String.valueOf(index));
                i.padStart(3);
                JsonObject obj = array.get(index).getAsJsonObject();
                String mime = obj.get("mime").getAsString();
                Path path = getPath(pathToImagesFolder, i.getVal() + "." + imageExtension);
                byte[] arr = Files.readAllBytes(path);
                Files.write(path, arr);

                jti.modifyImage(obj, index, pathToImagesFolder, mime);
                param = 1;
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
                throw new RuntimeException(ex);
            }
        });
        t2.start();
        try {
            t2.join();
        } catch (InterruptedException e) {
            param = 0;
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
        param = 0;
        System.err.println(e.getMessage());
        throw new RuntimeException(e);
    }
}
