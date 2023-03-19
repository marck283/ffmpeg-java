package test;

import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.VideoCreator;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.NotEnoughArgumentsException;

import org.apache.commons.text.StringEscapeUtils;

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

            /*VideoCreator creator = builder.newVideoCreator("\"./src/main/resources/it/disi/unitn/input/video/input.mp4\"",
                    "\"./src/main/resources/it/disi/unitn/input/images/InterazioneDatabasePerInfoUtenti.png\"",
                    "\"./src/main/resources/it/disi/unitn/input/images/Pattern_matching.png\"");
            creator.setVideoSize(800, 600);
            creator.setFrameRate(1);
            //creator.setCodecID("libx264");
            //creator.setVideoQuality(18);
            creator.createCommand();*/
            builder.setCommand("\"./lib/ffmpeg-fullbuild/bin/ffmpeg.exe\" -r 1 -s 800x600 " +
                    "-i \"./src/main/resources/it/disi/unitn/input/images/InterazioneDatabasePerInfoUtenti.png\" " +
                    "-i \"./src/main/resources/it/disi/unitn/input/images/Pattern_matching.png\" -pix_fmt yuv420p -c:v libx264 " +
                    //La riga seguente ritorna un errore quando si utilizza un codec che richiede che l'altezza
                    //sia divisibile per 2.
                    "-vf \"pad=ceil(iw/2)*2:ceil(ih/2)*2\" -y -v error " +
                    "\"./src/main/resources/it/disi/unitn/input/video/input.mp4\"");
            FFMpeg creationProcess = builder.build();
            creationProcess.executeCMD();
        } catch(NotEnoughArgumentsException /*| InvalidArgumentException*/ ex) {
            ex.printStackTrace();
        }
    }
}
