package it.disi.unitn.json;

//import ai.djl.translate.TranslateException;
import com.google.gson.*;
import it.disi.unitn.BigGAN.BigGANWrapper;
import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
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

    /**
     * The class's constructor. At the time of writing, this constructor only supports "image/jpeg" and "image/png" types.
     * Any other type will make this constructor throw an IllegalArgumentException.
     * @param pathToJsonFile The path to the JSON file on which to initialize an object of this class
     * @param useGAN A boolean value that, if true, instructs the program to use a GAN
     * @throws IllegalArgumentException When the specified format name is not supported
     * @throws IOException If an I/O error occurs opening the file
     */
    public JSONToImage(@NotNull String pathToJsonFile, boolean useGAN) throws IllegalArgumentException, IOException {
        File jsonFile = new File(pathToJsonFile);

        byteArrList = new ArrayList<>();
        Gson gson = new GsonBuilder().create();
        Reader r = Files.newBufferedReader(jsonFile.toPath().toAbsolutePath());
        JsonObject object = gson.fromJson(r, JsonObject.class);
        array = object.getAsJsonArray("array");
        this.useGAN = useGAN;
    }

    /**
     * This method transforms each of the elements of the array of JSON objects in the previously given file into an
     * array of bytes.
     */
    private void toByteArray() {
        for(JsonElement e: array) {
            JsonObject el = e.getAsJsonObject();
            String data = el.get("image-background").getAsString();
            data = data.replace("data:image/jpeg;base64,", "")
                    .replace("data:image/png;base64,", "");
            byteArrList.add(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)));
        }
    }

    /**
     * This method modifies the given image (denoted by the given path and the given array index) with the text obtained
     * by reading the JSON file that was given as input to this class's constructor.
     * @param obj The JSON object representing the current image
     * @param i An array index
     * @param pathToImagesFolder The path to the folder containing the input images
     * @param mime The image's MIME type
     * @throws IOException Se un errore di I/O si è verificato durante l'esecuzione di questo metodo
     */
    public void modifyImage(@NotNull JsonObject obj, int i, @NotNull String pathToImagesFolder, @NotNull String mime)
            throws IOException {
        try {
            JsonArray imageText = obj.getAsJsonArray("stats");
            StringExt i1ext = new StringExt(String.valueOf(i));
            i1ext.padStart();

            int positionx, positiony;
            float fontDim;
            for(JsonElement e1: imageText) {
                JsonObject textInfo = e1.getAsJsonObject();
                String text = textInfo.get("name").getAsString(), text1 = textInfo.get("value").getAsString();

                fontDim = textInfo.get("font").getAsFloat();
                positionx = textInfo.get("print-x").getAsInt();
                positiony = textInfo.get("print-y").getAsInt();
                addText(pathToImagesFolder + "/" + i1ext.getVal(), mime, text + ": " + text1,
                        positionx, positiony, fontDim, Color.BLACK);
            }

            imageText = obj.getAsJsonArray("entities");
            for (JsonElement e1 : imageText) {
                JsonObject textInfo = e1.getAsJsonObject();
                String text = textInfo.get("text").getAsString();

                fontDim = textInfo.get("font").getAsFloat();
                positionx = textInfo.get("print-x").getAsInt();
                positiony = textInfo.get("print-y").getAsInt();
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
     * @param obj The JsonObject instance that contains the "mime" attribute
     * @return The extension of the resulting images as a String instance
     * @throws UnsupportedEncodingException When the requested MIME type refers to a file encoding other than "image/jpeg"
     * or "image/png"
     */
    public @NotNull String getMIME(@NotNull JsonObject obj) throws UnsupportedEncodingException {
        String mime = obj.get("mime").getAsString(), imageExt;

        if(mime.equals("image/jpeg")) {
            imageExt = "jpeg";
        } else {
            if(mime.equals("image/png")) {
                imageExt = "png";
            } else {
                throw new UnsupportedEncodingException("An unsupported file extension was used.");
            }
        }

        return imageExt;
    }

    /**
     * This method instructs the library to generate the images with a GAN. The meanings of its parameters are the same
     * as for generate(). It is worth noting that this method will generate one image per description given by the user.
     * @param pathToImagesFolder The (either absolute or relative) path to the folder that will contain the generated images.
     * @param file The file to be used to instruct the GAN on what to include in the resulting images. This parameter
     *             must not be null if and only if the library is instructed to use a GAN
     * @param imageExtension The extension of the generated images
     * @param descriptions The categories to be represented in the resulting images. These categories must all be contained
     *                     in <a href="https://gist.github.com/yrevar/942d3a0ac09ec9e5eb3a">this file</a>.
     * @throws IOException If an error occurs when writing to or creating the file
     * //@throws TranslateException If the library is instructed to create images using a GAN and the generation fails
     */
    private void generateWithGAN(@NotNull String pathToImagesFolder, @NotNull String file, @NotNull String imageExtension,
                                 @NotNull String @NotNull ... descriptions) throws IOException, /*TranslateException,*/ InterruptedException {
        /*BigGANWrapper bgw = BigGANWrapper.create(file, descriptions);
        bgw.saveImages(pathToImagesFolder, bgw.generate(), imageExtension);

        File outDir = new File(pathToImagesFolder);
        if(outDir == null || !outDir.exists()) {
            throw new FileNotFoundException("The directory containing the images generated by the neural network " +
                    "does not exist. Please consider contacting the producer of this library to solve the problem.");
        }

        String[] content = outDir.list();
        if(content != null) {
            int len = content.length;
            for(int i = 0; i < len; ++i) {
                JsonObject obj = array.get(i).getAsJsonObject();
                //String mime = obj.get("mime").getAsString();
                String mime = "image/png";
                modifyImage(obj, i, pathToImagesFolder, mime);
            }
        } else {
            throw new NullPointerException("Either the given pathname does not represent a directory " +
                    "or an I/O has occurred.");
        }*/

        //Esegue il file che contiene il codice per la creazione delle immagini
        //Si noti che il codice Python contenuto in questo file non sarà eseguito perché il computer utilizzato
        //manca della potenza computazionale necessaria (i.e., di una GPU abbastanza potente)
        ProcessBuilder pb = new ProcessBuilder("bash -c \"python3 ./main.py\"");
        Process p = pb.start();
        p.wait();
    }

    /**
     * This method generates images from the previously obtained list of arrays of bytes.
     * WARNING: This method will fail to return the correct file type if the JSON array that is used in this class
     * contains images of different MIME types. Please insert only images of the same MIME type.
     * @param pathToImagesFolder The (either absolute or relative) path to the folder that will contain the generated images.
     * @param file The file to be used to instruct the GAN on what to include in the resulting images. This parameter
     *             must not be null if and only if the library is instructed to use a GAN
     * @param imageExtension The extension of the generated images
     * @param descriptions The categories to be represented in the resulting images. These categories must all be contained
     *                     in <a href="https://gist.github.com/yrevar/942d3a0ac09ec9e5eb3a">this file</a>. This parameter
     *                     must not be null if and only if the library is instructed to use a GAN
     * @throws IOException If an error occurs when writing to or creating the file
     * @throws InvalidArgumentException If a null or illegal value (e.e, the empty string) is passed as argument to this
     * method
     * //@throws TranslateException If the library is instructed to create images using a GAN and the generation fails
     * @throws InterruptedException If the current thread is interrupted by any other thread
     */
    public void generate(@NotNull String pathToImagesFolder, String file, @NotNull String imageExtension,
                           String ... descriptions) throws IOException, InvalidArgumentException,
            /*TranslateException,*/ InterruptedException {
        if(pathToImagesFolder == null || pathToImagesFolder.isEmpty()) {
            throw new IllegalArgumentException("A null or illegal value was passed as argument to this method.");
        }

        if(useGAN) {
            //Generate images using a GAN
            //First check that the parameters have correct values
            if(file == null || file.isEmpty() || descriptions == null) {
                throw new IllegalArgumentException("None of the arguments given to this method can be null or an " +
                        "empty string.");
            }
            for(String s: descriptions) {
                if(s == null || s.isEmpty()) {
                    throw new IllegalArgumentException("None of the descriptions can be null or an empty string.");
                }
            }

            //Then instruct the GAN to generate the images
            generateWithGAN(pathToImagesFolder, file, imageExtension, descriptions);
        } else {
            toByteArray();

            int i = 0;
            for(byte[] arr: byteArrList) {
                JsonObject obj = array.get(i).getAsJsonObject();
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
     * @param filePath The path of the given image
     * @param formatName The name of the file's format
     * @param inputText The text to be inserted into the given image
     * @param x The abscissa at which to add the given text
     * @param y The ordinate at which to add the given text
     * @param fontDim The font's dimension
     * @param color The font's color
     * @throws IOException if an error occurs during writing or when not able to create required ImageOutputStream
     */
    public void addText(@NotNull String filePath, @NotNull String formatName, @NotNull String inputText, int x, int y, float fontDim,
                        @NotNull Color color) throws IOException {
        String fname = "";
        if(formatName.equals("image/jpeg")) {
            fname = "jpeg";
        } else {
            if(formatName.equals("image/png")) {
                fname = "png";
            }
        }
        File file = new File(filePath + "." + fname);

        System.err.println("INPUT PATH: " + file.getPath());

        final BufferedImage image = ImageIO.read(file);  //Can't read input file... why?

        Graphics g = image.getGraphics();
        g.setFont(g.getFont().deriveFont(fontDim));
        g.setColor(color);
        g.drawString(inputText, x, y);
        g.dispose();

        ImageIO.write(image, fname, file);
    }
}
