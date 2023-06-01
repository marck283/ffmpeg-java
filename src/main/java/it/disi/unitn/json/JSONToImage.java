package it.disi.unitn.json;

import com.google.gson.*;
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

    /**
     * The class's constructor. At the time of writing, this constructor only supports "image/jpeg" and "image/png" types.
     * Any other type will make this constructor throw an IllegalArgumentException.
     * @param pathToJsonFile The path to the JSON file on which to initialize an object of this class
     * @throws IllegalArgumentException When the specified format name is not supported
     * @throws IOException If an I/O error occurs opening the file
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
            String data = el.get("image-background").getAsString();
            data = data.replace("data:image/jpeg;base64,", "")
                    .replace("data:image/png;base64,", "");
            byteArrList.add(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)));
        }
    }

    /**
     * This method generates images from the previously obtained list of arrays of bytes.
     * WARNING: This method will fail to return the correct file type if the JSON array that is used in this class
     * contains images of different MIME types. Please insert only images of the same MIME type.
     * @param pathToImagesFolder The (either absolute or relative) path to the folder that will contain the generated images.
     * @throws IOException If an error occurs when writing to or creating the file
     * @throws InvalidArgumentException If a null or illegal value (e.e, the empty string) is passed as argument to this
     * method
     * @return String The type of the generated images.
     */
    public String generate(@NotNull String pathToImagesFolder) throws IOException, InvalidArgumentException {
        if(pathToImagesFolder == null || pathToImagesFolder.equals("")) {
            throw new IllegalArgumentException("A null or illegal value was passed as argument to this method.");
        }

        toByteArray();

        int i = 0;
        String imageExt = "";
        for(byte[] arr: byteArrList) {
            Path path;

            JsonObject obj = array.get(i).getAsJsonObject();
            String mime = obj.get("mime").getAsString();
            StringExt fileName = new StringExt(String.valueOf(i));
            fileName.padStart();
            if(mime.equals("image/jpeg")) {
                path = Paths.get(pathToImagesFolder, fileName.getVal() + ".jpg");
                imageExt = "jpg";
            } else {
                if(mime.equals("image/png")) {
                    path = Paths.get(pathToImagesFolder, fileName.getVal() + ".png");
                    imageExt = "png";
                } else {
                    throw new UnsupportedEncodingException("An unsupported file extension was used.");
                }
            }
            Files.write(path, arr);

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
            i++;
        }

        return imageExt;
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
