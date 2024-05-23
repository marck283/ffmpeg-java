package it.disi.unitn.json;

import com.google.gson.JsonArray;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.json.jsonparser.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.exit;

class JSONToImageTest {

    @Test
    void generate() throws IOException {
        String jsonFile = "./src/test/resources/input/json/imageArray.json";
        Path p = Paths.get(jsonFile).toAbsolutePath();
        File f = new File(jsonFile);
        if(!f.exists()) {
            System.err.println("NONEXISTENT FILE");
            exit(1);
        }
        if(!f.canRead()) {
            System.err.println("CANNOT READ FILE");
            exit(1);
        }
        try(Reader reader = Files.newBufferedReader(p)) {
            JsonParser parser = new JsonParser(reader);
            JsonArray array = parser.getJsonArray("array");
            JSONToImage json2image = new JSONToImage(jsonFile, false);
            String imageExt = json2image.getMIME(array.get(0).getAsJsonObject());
            json2image.getFontFromJSON(parser);
            json2image.generate("./src/test/resources/input/images", imageExt, 800, 600, 1800000);
        } catch (InvalidArgumentException | InterruptedException e) {
            System.err.println(e.getMessage());
            //throw new RuntimeException(e);
        }
    }
}