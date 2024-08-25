package it.disi.unitn.lasagna;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.PermissionsException;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class extends java.io.File to provide further functionality.
 */
public class File extends java.io.File {

    /**
     * The path to the file the user wants to identify
     */
    private final String pathname;

    /**
     * The class's constructor.
     * @param pathname The path to the file the user wants to identify
     */
    public File(@NotNull String pathname) {
        super(pathname);
        this.pathname = pathname;
    }

    /**
     * Crea un'istanza della classe Path risolvendo il pathnme e il file passati.
     * @param path Il pathname da utilizzare
     * @param filename Il nome del file da risolvere
     * @return Un'istanza di Path che rappresenta il file passato come parametro.
     */
    public static @NotNull Path getPath(String path, String filename) {
        Path path1 = Paths.get(path);
        return path1.resolve(filename);
    }

    /**
     * This method checks the reading and writing permissions on the folder identified by the given parameter.
     * @throws PermissionsException If the user does not have reading or writing permissions on the given folder
     */
    public void checkReadWritePermissions() throws PermissionsException {
        try {
            Path path = getPath(pathname, "");
            if(!Files.isReadable(path)) {
                throw new PermissionsException("Cannot read the content of this directory.", "Non e' possibile " +
                        "leggere il contenuto di questa directory.");
            }
            if(!Files.isWritable(path)) {
                throw new PermissionsException("Cannot write to this directory.", "Non e' possibile scrivere su questa " +
                        "directory.");
            }
        } catch(InvalidArgumentException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
}
