package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.TracksMerger;
import it.disi.unitn.VideoCreator;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import it.disi.unitn.json.JSONToImage;
import test.audio.Audio;
import test.audio.Description;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

public class Main {

    private static String padStart(@NotNull String val) {
        if(val.length() == 0 || val.length() > 3) {
            throw new IllegalArgumentException("La lunghezza della stringa fornita è nulla o maggiore di 3.");
        }

        int missing = 3 - val.length();
        if(missing > 0) {
            StringBuilder valBuilder = new StringBuilder(val);
            for(int i = 0; i < missing; i++) {
                valBuilder.insert(0, "0");
            }
            val = valBuilder.toString();
        }

        return val;
    }

    public static void main(String[] args) {
        File f = new File("./src/main/resources/it/disi/unitn/input/json/imageArray.json");

        //La conversione in path assoluto è necessaria perché il file di esempio non è nel classpath
        Path p = Path.of(f.toPath().toAbsolutePath().toString());
        try (Reader reader = Files.newBufferedReader(p)) {
            Gson gson = new GsonBuilder().create();
            JsonObject json = gson.fromJson(reader, JsonObject.class);

            int i = 0, numAudioFiles;
            for(JsonElement e: json.getAsJsonArray("array")) {
                Description description = Description.parseJSON(e.getAsJsonObject());

                Audio audio = new Audio(description.getDescription(), description.getLanguage());
                audio.getOutput(i);
                i++;
            }
            numAudioFiles = i;

            File directory = new File("./src/main/resources/it/disi/unitn/input/images");
            boolean created = directory.mkdirs();

            JSONToImage json2Image = new JSONToImage(f.getPath());
            json2Image.generate("png", "./src/main/resources/it/disi/unitn/input/images");
            json2Image.addText("./src/main/resources/it/disi/unitn/input/images/000.png", "png", "Hello, world!",
                    100, 100, 30f, Color.BLACK);

            try {
                final FFMpegBuilder builder = new FFMpegBuilder("\"./lib/ffmpeg-fullbuild/bin/ffmpeg.exe\"");
                TracksMerger unitnMerger;

                try {
                    //Con pattern dei file
                    /*VideoCreator creator = builder.newVideoCreator("\"./src/main/resources/it/disi/unitn/input/video/input.mp4\"",
                            "./src/main/resources/it/disi/unitn/input/images", "%03d.png");*/

                    //Con nome preciso del file
                    for(i = 0; i < numAudioFiles; i++) {
                        String fileName = padStart(String.valueOf(i));
                        VideoCreator creator = builder.newVideoCreator("\"./src/main/resources/it/disi/unitn/input/video/" +
                                        fileName + ".mp4\"",
                                "./src/main/resources/it/disi/unitn/input/images", fileName + ".png");
                        creator.setVideoSize(800, 600);
                        creator.setFrameRate(1);
                        creator.setCodecID("libx264");
                        creator.setVideoQuality(18);
                        creator.createCommand();

                        FFMpeg creationProcess = builder.build();
                        creationProcess.executeCMD(30L, TimeUnit.SECONDS);

                        builder.setCommand("\"./lib/ffmpeg-fullbuild/bin/ffmpeg.exe\"");

                        unitnMerger = builder.newTracksMerger(
                                "\"./src/main/resources/it/disi/unitn/output/partial/" + fileName + ".mp4\"",
                                "\"./src/main/resources/it/disi/unitn/input/audio/" + fileName + ".mp3\"",
                                "\"./src/main/resources/it/disi/unitn/input/video/" + fileName + ".mp4\"");
                        unitnMerger.streamCopy(true);
                        unitnMerger.mergeAudioWithVideo();

                        FFMpeg process = builder.build();
                        process.executeCMD(1L, TimeUnit.MINUTES);
                    }

                    File outputDir = new File("./src/main/resources/it/disi/unitn/output/partial");
                    File[] fileList = outputDir.listFiles();

                    List<String> filePathList = new ArrayList<>();
                    for(i = 0; i < fileList.length; i++) {
                        filePathList.add(fileList[i].getPath());
                    }

                    //Il comando di merge dei video prende in input i percorsi dal file "inputFile.txt", ma questi
                    //percorsi sono da intendersi come percorsi relativi alla directory in cui il "inputFile.txt" è contenuto!
                    builder.setCommand("\"./lib/ffmpeg-fullbuild/bin/ffmpeg.exe\"");
                    unitnMerger = builder.newTracksMerger("./src/main/resources/it/disi/unitn/output/output.mp4");
                    unitnMerger.streamCopy(true);
                    unitnMerger.mergeVideos(filePathList);

                    FFMpeg process = builder.build();
                    process.executeCMD(1L, TimeUnit.MINUTES);

                    FileUtils.cleanDirectory(directory);
                    boolean deleted = directory.delete();
                    if(!deleted) {
                        System.err.println("La cartella \"images\" non può essere eliminata perché non è vuota.");
                        System.exit(1); //Termina il programma con codice di errore 1.
                    }
                } catch (NotEnoughArgumentsException | InvalidArgumentException ex) {
                    System.err.println(ex.getMessage());
                }
            } catch (NotEnoughArgumentsException ex) {
                System.err.println(ex.getMessage());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
        }
    }
}