package it.disi.unitn.json;

import com.google.gson.*;
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

    /**
     * The class's constructor. At the time of writing, this constructor only supports "image/jpeg" and "image/png" types.
     * Any other type will make this constructor throw an IllegalArgumentException.
     * @param pathToJsonFile The path to the JSON file on which to initialize an object of this class
     * @throws IllegalArgumentException When the specified format name is not supported
     */
    public JSONToImage(@NotNull String pathToJsonFile) throws IllegalArgumentException, IOException {
        /**
         * The JSON file
         */
        File jsonFile = new File(pathToJsonFile);

        byteArrList = new ArrayList<>();
        Gson gson = new GsonBuilder().create();
        Reader r = Files.newBufferedReader(jsonFile.toPath().toAbsolutePath());
        JsonObject object = gson.fromJson(r, JsonObject.class);
        array = object.getAsJsonArray("array");
    }

    /**
     * This method transforms each of the elements of the array of JSON objects in the previously given file into an
     * array of bytes.
     */
    private void toByteArray() {
        for(JsonElement e: array) {
            JsonObject el = e.getAsJsonObject();
            String data = el.get("data").getAsString();
            data = data.replace("data:image/jpeg;base64", "")
                    .replace("data:image/png;base64", "");
            byteArrList.add(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)));
        }
    }

    private String padStart(@NotNull String fileName) {
        if(fileName == null || fileName.equals("")) {
            throw new IllegalArgumentException("An illegal value was passed as argument to this method.");
        }

        int missing = 3 - fileName.length();
        if(missing > 0) {
            StringBuilder fileNameBuilder = new StringBuilder(fileName);
            for(int i = 0; i < missing; i++) {
                fileNameBuilder.insert(0, "0");
            }
            fileName = fileNameBuilder.toString();
        }

        return fileName;
    }

    /**
     * This method generates images from the previously obtained list of arrays of bytes.
     * @param pathToImagesFolder The (either absolute or relative) path to the folder that will contain the generated images.
     * @throws IOException If an error occurs when writing to or creating the file
     */
    public void generate(@NotNull String pathToImagesFolder) throws IOException {
        if(pathToImagesFolder == null || pathToImagesFolder.equals("")) {
            throw new IllegalArgumentException("A null or illegal value was passed as argument to this method.");
        }

        toByteArray();

        int i = 0;
        for(byte[] arr: byteArrList) {
            Path path;

            JsonObject obj = array.get(i).getAsJsonObject();
            String mime = obj.get("mime").getAsString();
            if(mime.equals("image/jpeg")) {
                path = Paths.get(pathToImagesFolder, padStart(String.valueOf(i)) + ".jpg");
            } else {
                if(mime.equals("image/png")) {
                    path = Paths.get(pathToImagesFolder, padStart(String.valueOf(i)) + ".png");
                } else {
                    throw new UnsupportedEncodingException("An unsupported file extension was used.");
                }
            }
            Files.write(path, arr);
            i++;
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
        if(formatName.equals("image/jpeg")) {
            formatName = "jpg";
        } else {
            if(formatName.equals("image/png")) {
                formatName = "png";
            }
        }
        File file = new File(filePath + "." + formatName);
        final BufferedImage image = ImageIO.read(file);

        Graphics g = image.getGraphics();
        g.setFont(g.getFont().deriveFont(fontDim));
        g.setColor(color);
        g.drawString(inputText, x, y);
        g.dispose();

        ImageIO.write(image, formatName, file);
    }
}
