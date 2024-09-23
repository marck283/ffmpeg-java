package it.disi.unitn.transition.rotation;

import it.disi.unitn.StringExt;
import it.disi.unitn.lasagna.File;
import it.disi.unitn.transitions.rotation.RotationTransition;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class RotationTest {

    @Test
    public void test() throws Exception {
        String tempOutDir = "./src/test/resources/output/images/rotation";
        File.makeDirs(tempOutDir);

        RotationTransition rotation = null;
        StringExt str = new StringExt(String.valueOf(0)), str1;
        str.padStart(3);
        for(int i = 0; i < 40; i++) {
            str1 = new StringExt(String.valueOf(i));
            str1.padStart(3);
            rotation = new RotationTransition("./src/test/resources/input/images/" + str.getVal() + ".jpeg",
                    tempOutDir,
                    "./src/test/resources/output/video/rotation", "jpg");

            rotation.rotate(400, 400, i + 1, "test", str1.getVal(), Color.BLACK);
        }
        rotation.performRotation(1L, TimeUnit.MINUTES, 800, 600, "output", "mjpeg",
                "mp4");
        rotation.dispose();
    }

}
