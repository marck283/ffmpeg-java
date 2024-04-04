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
     * @throws InvalidArgumentException If any of the arguments given to this constructor is null or an empty string
     */
    public ExecutorResHandler(@NotNull JsonArray arr, int i, @NotNull String pathToImagesFolder,
                              @NotNull String imageExtension, @NotNull JSONToImage jti, @NotNull ProcessPool pp)
            throws InvalidArgumentException {
        if (arr == null || pathToImagesFolder == null || pathToImagesFolder.isEmpty() || imageExtension == null ||
                imageExtension.isEmpty() || jti == null || pp == null) {
            throw new InvalidArgumentException("No argument to this constructor can be null or an empty string.",
                    "Nessuno degli argomenti forniti a questo costruttore puo' essere null o una stringa vuota.");
        }
        array = arr;
        index = i;
        this.pathToImagesFolder = pathToImagesFolder;
        this.imageExtension = imageExtension;
        this.jti = jti;
        this.pp = pp;
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
        return Paths.get(first, path);
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
                i.padStart();
                JsonObject obj = array.get(index).getAsJsonObject();
                String mime = obj.get("mime").getAsString();
                Path path = getPath(pathToImagesFolder, i.getVal() + "." + imageExtension);
                byte[] arr = Files.readAllBytes(path);
                Files.write(path, arr);

                jti.modifyImage(obj, index, pathToImagesFolder, mime);
                pp.removeHandler(this);
            } catch (InvalidArgumentException | IOException ex) {
                //ex.printStackTrace();
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
        //e.printStackTrace();
        System.err.println(e.getMessage());
        System.exit(1);
    }
}
