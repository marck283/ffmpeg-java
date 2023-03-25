package test;

import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.TracksMerger;
import it.disi.unitn.VideoCreator;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import it.disi.unitn.json.JSONToImage;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Main sample class
 */
public class Main {

    /**
     * Default constructor
     */
    public Main() {
        //Default constructor
    }

    /**
     * Main method
     * @param args List of arguments given to the program
     */
    public static void main(String[] args) {
        try {
            FFMpegBuilder builder = new FFMpegBuilder("\"./lib/ffmpeg-fullbuild/bin/ffmpeg.exe\"");

            JSONToImage json2Image = new JSONToImage("./src/main/resources/it/disi/unitn/input/json/imageArray.json");
            json2Image.generate("png", "./src/main/resources/it/disi/unitn/input/images");
            json2Image.addText("./src/main/resources/it/disi/unitn/input/images/000.png", "Hello, world!",
                    100, 100, 30f, Color.BLACK);

            VideoCreator creator = builder.newVideoCreator("\"./src/main/resources/it/disi/unitn/input/video/input.mp4\"",
                    "./src/main/resources/it/disi/unitn/input/images", "%03d.png");
            creator.setVideoSize(800, 600);
            creator.setFrameRate(1);
            creator.setCodecID("libx264");
            creator.setVideoQuality(18);
            creator.createCommand();

            //Programmare cancellazione file immagini dopo la creazione del video intermedio.

            FFMpeg creationProcess = builder.build();
            creationProcess.executeCMD(30L, TimeUnit.SECONDS);

            builder.setCommand("\"./lib/ffmpeg-fullbuild/bin/ffmpeg.exe\"");

            TracksMerger unitnMerger = builder.newTracksMerger(
                    "\"./src/main/resources/it/disi/unitn/output/output.mp4\"",
                    "\"./src/main/resources/it/disi/unitn/input/audio/input.mp3\"",
                    "\"./src/main/resources/it/disi/unitn/input/video/input.mp4\"");
            unitnMerger.streamCopy(true);
            unitnMerger.merge();
            creationProcess = builder.build();
            creationProcess.executeCMD(15L, TimeUnit.SECONDS);
        } catch(NotEnoughArgumentsException | InvalidArgumentException | IOException ex) {
            ex.printStackTrace();
        }
    }
}
