package it.disi.unitn.lasagna;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.PermissionsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * This class extends java.io.File to provide further functionality.
 */
public class MyFile extends java.io.File {

    /**
     * The path to the file the user wants to identify
     */
    private final String pathname;

    /**
     * The class's constructor.
     * @param pathname The path to the file the user wants to identify
     */
    public MyFile(@NotNull String pathname) {
        super(pathname);
        this.pathname = pathname;
    }

    /**
     * Crea un'istanza della classe Path risolvendo il pathnme e il file passati.
     * @param path Il pathname da utilizzare
     * @param filename Il nome del file da risolvere
     * @return Un'istanza di Path che rappresenta il file passato come parametro.
     */
    public static Path getPath(String path, String filename) {
        Path path1 = Paths.get(path);
        return path1.resolve(filename);
    }

    /**
     * Questo metodo pu&ograve; essere utilizzato per creare le directory dai path comunicati come argomenti.
     * @param dirPaths I path da utilizzare per creare le directory necessarie
     * @throws Exception Quando almeno uno dei valori passati come argomento &egrave; null o, se l'utente sta operando
     * su un sistema Linux, il processo di creazione di ua cartella fallisce.
     */
    public static void makeDirs(@NotNull String @NotNull ... dirPaths)
            throws Exception {
        if(dirPaths == null || Arrays.stream(dirPaths).anyMatch(StringExt::checkNullOrEmpty)) {
            throw new IllegalArgumentException("Nessuno dei valori passati a questo metodo puo' essere null o una " +
                    "stringa vuota.");
        }

        for(String path: dirPaths) {
            Files.createDirectories(getPath(path, ""));
        }
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

    /**
     * Questo metodo rimuove tutte le cartelle interne all'albero avente come radice la cartella associata al path
     * comunicato in fase di istanziazione della classe.
     * @throws IllegalArgumentException Quando almeno un argomento è null
     * @throws IOException se occorre un errore I/O
     */
    public void removeSelf() throws IllegalArgumentException, IOException {
        removeContent(null);
    }

    /**
     * This method removes all files except the ones whose name contains the given value.
     * @param fileName The given file name. This value cannot be null or an empty string.
     * @throws IOException If the file cannot be deleted for whatever reason.
     * @throws InvalidArgumentException If the given argument is null or an empty string
     */
    public void removeContentExceptFile(@NotNull String fileName) throws IOException, InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(fileName)) {
            throw new InvalidArgumentException("The given file name cannot be null or an empty string.", "Il nome del file " +
                    "fornito non puo' essere null o una stringa vuota.");
        }
        try(Stream<Path> walk = Files.walk(getPath(pathname, ""))) {
            walk.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    if(path.toFile().isFile() && !path.getFileName().toString().contains(fileName)) {
                        Files.delete(path);
                    }
                } catch (IOException ex) {
                    System.err.println("IOException: " + ex.getMessage());
                }
            });
        }
    }

    /**
     * This method removes the content of a folder. It can also be used to delete files with specific extensions.
     * @param fileExt The given file extension. This value can be null or an empty string.
     * @throws IOException If any file cannot be deleted for whatever reason.
     */
    public void removeContent(@Nullable String fileExt) throws IOException {
        try(Stream<Path> walk = Files.walk(getPath(pathname, ""))) {
            walk.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    if(StringExt.checkNullOrEmpty(fileExt) || path.getFileName().toString().endsWith(fileExt)) {
                        Files.delete(path);
                    }
                } catch (IOException ex) {
                    System.err.println("IOException: " + ex.getMessage());
                }
            });
        }
    }

    /**
     * Questo metodo permette di ottenere una lista dei path dei file interni alla cartella identificata da questa
     * istanza di File.
     * @return La lista dei path dei file interni alla cartella associata a questa istanza di File
     * @throws IOException Se si verifica un errore di I/O
     */
    public List<String> getFileList() throws IOException {
        if(!Files.isDirectory(getPath(getPath(), ""))) {
            throw new FileNotFoundException("Il percorso fornito non denota una directory.");
        }

        List<String> filePathList = new ArrayList<>();
        try (Stream<Path> pathStream = Files.list(getPath(pathname, ""))) {
            if(pathStream == null) {
                throw new FileNotFoundException("Il percorso fornito non denota una directory.");
            }
            pathStream.forEach(path -> filePathList.add(path.toString()));
        }

        return filePathList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyFile myFile)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(pathname, myFile.pathname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pathname);
    }
}
