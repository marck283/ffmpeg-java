package it.disi.unitn.json;

import com.google.gson.*;
import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
//import it.disi.unitn.exceptions.ProcessStillAliveException;
import it.disi.unitn.json.processpool.ProcessPool;
/*import it.disi.unitn.streamhandlers.InputHandler;
import org.apache.commons.lang3.SystemUtils;*/
import it.disi.unitn.json.jsonparser.JsonParser;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
/*import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;*/

/**
 * This class translates a given JSON file into a list of Image objects.
 */
public class JSONToImage {

    /**
     * The list of arrays of bytes representing the images
     */
    private final List<byte[]> byteArrList;

    private final JsonArray array;

    private final boolean useGAN;

    private final JsonParser parser;

    /**
     * The class's constructor. At the time of writing, this constructor only supports "image/jpeg" and "image/png" types.
     * Any other type will make this constructor throw an InvalidArgumentException.
     *
     * @param pathToJsonFile The path to the JSON file on which to initialize an object of this class
     * @param useGAN A boolean value that, if true, instructs the program to use a GAN
     * @throws IOException If an I/O error occurs opening the file
     * @throws InvalidArgumentException If the first parameter of this method is null or an empty string
     */
    public JSONToImage(@NotNull String pathToJsonFile, boolean useGAN) throws IOException, InvalidArgumentException {
        if(pathToJsonFile == null || pathToJsonFile.isEmpty()) {
            throw new InvalidArgumentException("The first parameter of this method cannot be null or an empty string.",
                    "Il primo parametro passato a questo metodo non puo' essere null o una stringa vuota.");
        }
        File jsonFile = new File(pathToJsonFile);

        byteArrList = new ArrayList<>();
        Gson gson = new GsonBuilder().create();
        Reader r = Files.newBufferedReader(jsonFile.toPath().toAbsolutePath());
        JsonObject object = gson.fromJson(r, JsonObject.class);
        parser = new JsonParser(r);
        array = object.getAsJsonArray("array");
        this.useGAN = useGAN;
    }

    /**
     * This method transforms each of the elements of the array of JSON objects in the previously given file into an
     * array of bytes.
     * @throws InvalidArgumentException If any of the elements of the JSON array taken from the input file is null or an
     * empty string
     */
    private void toByteArray() throws InvalidArgumentException {
        for (JsonElement e : array) {
            String data = parser.getString(e, "image-background")
                    .replace("data:image/jpeg;base64,", "")
                    .replace("data:image/png;base64,", "");
            byteArrList.add(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)));
        }
    }

    /**
     * This method modifies the given image (denoted by the given path and the given array index) with the text obtained
     * by reading the JSON file that was given as input to this class's constructor.
     *
     * @param obj The JSON object representing the current image
     * @param i An array index
     * @param pathToImagesFolder The path to the folder containing the input images
     * @param mime The image's MIME type
     * @throws IOException If an I/O error happened during this method's execution
     */
    public void modifyImage(@NotNull JsonObject obj, int i, @NotNull String pathToImagesFolder, @NotNull String mime)
            throws IOException {
        try {
            if(obj == null || i < 0 || pathToImagesFolder == null || pathToImagesFolder.isEmpty() || mime == null ||
                    mime.isEmpty()) {
                throw new InvalidArgumentException("None of the arguments given to this method can be null, less than zero " +
                        "or an empty string.", "Nessuno degli argomenti forniti a questo metodo puo' essere null, minore " +
                        "di zero o una stringa vuota.");
            }
            JsonArray imageText = obj.getAsJsonArray("stats");
            StringExt i1ext = new StringExt(String.valueOf(i));
            i1ext.padStart();

            int positionx, positiony;
            float fontDim;
            for (JsonElement e1 : imageText) {
                //JsonObject textInfo = e1.getAsJsonObject();
                //String text = textInfo.get("name").getAsString(), text1 = textInfo.get("value").getAsString();
                String text = parser.getString(e1, "name"), text1 = parser.getString(e1, "value");

                //fontDim = textInfo.get("font").getAsFloat();
                fontDim = parser.getFloat(e1, "font");
                //positionx = textInfo.get("print-x").getAsInt();
                positionx = parser.getInt(e1, "print-x");
                //positiony = textInfo.get("print-y").getAsInt();
                positiony = parser.getInt(e1, "print-y");
                addText(pathToImagesFolder + "/" + i1ext.getVal(), mime, text + ": " + text1,
                        positionx, positiony, fontDim, Color.BLACK);
            }

            imageText = obj.getAsJsonArray("entities");
            for (JsonElement e1 : imageText) {
                //JsonObject textInfo = e1.getAsJsonObject();
                //String text = textInfo.get("text").getAsString();
                String text = parser.getString(e1, "text");

                //fontDim = textInfo.get("font").getAsFloat();
                fontDim = parser.getFloat(e1, "font");
                //positionx = textInfo.get("print-x").getAsInt();
                positionx = parser.getInt(e1, "print-x");
                //positiony = textInfo.get("print-y").getAsInt();
                positiony = parser.getInt(e1, "print-y");
                addText(pathToImagesFolder + "/" + i1ext.getVal(), mime, text,
                        positionx, positiony, fontDim, Color.BLACK);
            }
        } catch (InvalidArgumentException ex) {
            System.err.println(ex.getMessage());
            System.err.println("Si prega di contattare il produttore di questa libreria per risolvere il problema.");
        }
    }

    /**
     * This method returns the extension of the resulting images by reading the MIME type requested by the user
     * through a JsonObject instance.
     *
     * @param obj The JsonObject instance that contains the "mime" attribute
     * @return The extension of the resulting images as a String instance
     * @throws UnsupportedEncodingException When the requested MIME type refers to a file encoding other than "image/jpeg"
     *                                      or "image/png"
     */
    public @NotNull String getMIME(@NotNull JsonObject obj) throws UnsupportedEncodingException {
        String mime = obj.get("mime").getAsString(), imageExt;

        if (mime.equals("image/jpeg")) {
            imageExt = "jpeg";
        } else {
            if (mime.equals("image/png")) {
                imageExt = "png";
            } else {
                Locale l = Locale.getDefault();
                if(l == Locale.ITALIAN || l == Locale.ITALY) {
                    throw new UnsupportedEncodingException("E' stata usata un'estensione non supportata.");
                } else {
                    throw new UnsupportedEncodingException("An unsupported file extension was used.");
                }
            }
        }

        return imageExt;
    }

    /**
     * This method instructs the library to generate the images with a GAN. The meanings of its parameters are the same
     * as for generate(). It is worth noting that this method will generate one image per description given by the user.
     *
     * @param pathToImagesFolder The (either absolute or relative) path to the folder that will contain the generated images.
     * @param imageExtension The extension of the generated images
     * @param width The width of the generated images
     * @param height The height of the generated images
     * @param timeout The given timeout in milliseconds
     * @throws InterruptedException if a thread is interrupted during its execution
     * @throws InvalidArgumentException If any of the elements of the JSON array taken from the input file is null or an
     * empty string, or either the given width or height is less than or equal to zero or not divisible by both 8 and 64
     */
    private void generateWithGAN(@NotNull String pathToImagesFolder, @NotNull String imageExtension,
                                 int width, int height, long timeout) throws InterruptedException, InvalidArgumentException {
        if (width % 8 != 0 || height % 8 != 0) {
            throw new InvalidArgumentException("Both the image's width and height must be divisible by 8.", "Sia l'ampiezza " +
                    "che l'altezza dell'immagine devono essere divisibili per 8.");
        }

        if (width % 64 != 0 || height % 64 != 0) {
            throw new InvalidArgumentException("Both the image's width and height must be divisible by 64.", "Sia l'ampiezza " +
                    "che l'altezza dell'immagine devono essere divisibili per 64.");
        }

        int index = 0;
        Path dir = Paths.get(""); //Gets the current working directory
        File model = dir.resolve("model.py").toFile();
        ProcessPool pool = new ProcessPool(model, imageExtension, pathToImagesFolder, width, height);
        for (JsonElement e : array) {
            String desc = parser.getString(e, "image-description");
            pool.setDesc(desc);
            pool.setIndex(index);
            pool.execute(array, this, timeout);

            index = index + 1;
        }

        Thread t1 = new Thread(() -> {
            try {
                //Waits until this thread is notified. This method needs to be synchronized for this call to work properly.
                pool.doWait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t1.start();
        t1.join();

        System.out.println("EMPTY LIST!");
    }

    /**
     * This method generates images from the previously obtained list of arrays of bytes.
     * WARNING: This method will fail to return the correct file type if the JSON array that is used in this class
     * contains images of different MIME types. Please insert only images of the same MIME type.
     *
     * @param pathToImagesFolder The (either absolute or relative) path to the folder that will contain the generated images.
     * @param imageExtension The extension of the generated images
     * @param width The width of the generated images. This value should be positive if the program uses a
     *              neural network to generate the images
     * @param height The height of the generated images. This value should be positive if the program uses a
     *               neural network to generate the images
     * @param timeout The given timeout in milliseconds
     * @throws IOException If an error occurs when writing to or creating the file
     * @throws InvalidArgumentException If a null or illegal value (e.e, the empty string) is passed as argument to this
     * @throws InterruptedException If the current thread is interrupted while waiting
     * @throws InvalidArgumentException If either the first two parameters an null or empty strings, or the last three
     * are less than or equal to zero
     */
    public void generate(@NotNull String pathToImagesFolder, @NotNull String imageExtension, int width, int height,
                         long timeout)
            throws IOException, InvalidArgumentException, InterruptedException {
        System.out.println(pathToImagesFolder);
        System.out.println(imageExtension);
        System.out.println(width);
        System.out.println(height);
        System.out.println(timeout);
        if (pathToImagesFolder == null || pathToImagesFolder.isEmpty() || imageExtension == null ||
                imageExtension.isEmpty() || width <= 0 || height <= 0 || timeout <= 0) {
            throw new InvalidArgumentException("A null or illegal value was passed as argument to this method.", "Almeno " +
                    "uno dei parametri ha valore null o ha un valore non consentito.");
        }

        if (useGAN) {
            //Avvia la rete neurale per produrre le immagini
            generateWithGAN(pathToImagesFolder, imageExtension, width, height, timeout);
        } else {
            toByteArray();

            int i = 0;
            for (byte[] arr : byteArrList) {
                JsonObject obj = parser.getJsonObject(array.get(i));
                String mime = obj.get("mime").getAsString();
                StringExt fileName = new StringExt(String.valueOf(i));
                fileName.padStart();
                Path path = Paths.get(pathToImagesFolder, fileName.getVal() + "." + imageExtension);
                Files.write(path, arr);

                modifyImage(obj, i, pathToImagesFolder, mime);
                i++;
            }
        }
    }

    /**
     * Adds the given text to the image associated to the given file path starting at the point (x, y). This method also
     * allows the user to specify the font's dimension and its color.
     *
     * @param filePath The path of the given image
     * @param formatName The name of the file's format
     * @param inputText The text to be inserted into the given image
     * @param x The abscissa at which to add the given text
     * @param y The ordinate at which to add the given text
     * @param fontDim The font's dimension
     * @param color The font's color
     * @throws IOException if an error occurs during writing or when not able to create required ImageOutputStream
     * @throws InvalidArgumentException If any of the arguments passed to this method is null, less than zero or an empty
     * string
     */
    public void addText(@NotNull String filePath, @NotNull String formatName, @NotNull String inputText, int x, int y,
                        float fontDim, @NotNull Color color) throws IOException, InvalidArgumentException {
        if(filePath == null || filePath.isEmpty() || formatName == null || formatName.isEmpty() || inputText == null ||
        inputText.isEmpty() || x < 0 || y < 0 || color == null) {
            throw new InvalidArgumentException("None of the parameters passed to this method can be null, less than zero " +
                    "or an empty string.", "Nessuno dei parametri forniti a questo metodo puo' essere null, minore di zero " +
                    "o una stringa vuota.");
        }
        if(Float.max(fontDim, 0.0F) == 0.0F) {
            throw new InvalidArgumentException("The font's dimension cannot be less than or equal to zero.", "La dimensione " +
                    "del font non puo' essere minore o uguale a zero.");
        }
        String fname = "";
        if (formatName.equals("image/jpeg")) {
            fname = "jpeg";
        } else {
            if (formatName.equals("image/png")) {
                fname = "png";
            }
        }
        File file = new File(filePath + "." + fname);

        final BufferedImage image = ImageIO.read(file);

        Graphics g = image.getGraphics();
        //g.setFont(<font>); //Per font personalizzati
        g.setFont(g.getFont().deriveFont(fontDim));
        g.setColor(color);
        g.drawString(inputText, x, y);
        g.dispose();

        ImageIO.write(image, fname, file);
    }
}
