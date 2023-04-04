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
     * The JSON file
     */
    private final File jsonFile;

    /**
     * The list of arrays of bytes representing the images
     */
    private final List<byte[]> byteArrList;

    /**
     * The class's constructor
     * @param pathToJsonFile The path to the JSON file on which to initialize an object of this class
     */
    public JSONToImage(@NotNull String pathToJsonFile) {
        jsonFile = new File(pathToJsonFile.replace("\\", "/"));
        byteArrList = new ArrayList<>();
    }

    /**
     * This method transforms each of the elements of the array of JSON objects in the previously given file into an
     * array of bytes.
     * @throws IOException if an I/O error occurs when opening the file
     */
    private void toByteArray() throws IOException {
        try (Reader r = Files.newBufferedReader(jsonFile.toPath().toAbsolutePath())) {
            Gson gson = new GsonBuilder().create();
            JsonObject object = gson.fromJson(r, JsonObject.class);
            JsonArray jsonArray1 = object.getAsJsonArray("array");

            for(JsonElement e: jsonArray1) {
                JsonObject el = e.getAsJsonObject();
                String data = el.get("data").getAsString();
                data = data.replace("data:image/jpeg;base64", "")
                        .replace("data:image/png;base64", "");
                byteArrList.add(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)));
            }
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
     * @param formatName The images' format names. ATTENTION: this parameter must be either "jpg" or "png".
     *                   Other format names are not currently accepted and will make this method throw IllegalArgumentException.
     * @param pathToImagesFolder The (either absolute or relative) path to the folder that will contain the generated images.
     * @throws IOException if an error occurs during reading or when not able to create required ImageInputStream
     */
    public void generate(@NotNull String formatName, @NotNull String pathToImagesFolder) throws IOException {
        if(pathToImagesFolder == null || pathToImagesFolder.equals("") || formatName == null || formatName.equals("") ||
                (formatName != null && !formatName.equals("") && !formatName.equals("jpg") && !formatName.equals("png"))) {
            throw new IllegalArgumentException("A null or illegal value was passed as argument to this method.");
        }

        toByteArray();

        int i = 0;
        for(byte[] arr: byteArrList) {
            Path path = Paths.get(pathToImagesFolder, padStart(String.valueOf(i)) + "." + formatName);
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
        File file = new File(filePath);
        final BufferedImage image = ImageIO.read(file);

        Graphics g = image.getGraphics();
        g.setFont(g.getFont().deriveFont(fontDim));
        g.setColor(color);
        g.drawString(inputText, x, y);
        g.dispose();

        ImageIO.write(image, formatName, file);
    }
}
