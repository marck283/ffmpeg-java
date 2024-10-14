package it.disi.unitn.transition.rotation;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import it.disi.unitn.lasagna.File;
import it.disi.unitn.transitions.rotation.RotationTransition;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.ScalingParams;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.scalingalgs.Bicubic;
import org.jetbrains.annotations.NotNull;
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
        int j = 0;
        for(int i = 1; i < 30; i++) {
            str1 = new StringExt(String.valueOf(i));
            str1.padStart(3);
            rotation = new RotationTransition("./src/test/resources/input/images/" + str.getVal() + ".jpeg",
                    tempOutDir,
                    "src/test/resources/output/video/rotation", "jpg");

            rotation.rotate(400, 400, j + 1, "test", str1.getVal(), Color.BLACK);
            j++;
        }

        rotation.performRotation(1L, TimeUnit.MINUTES, "output", "mp4", getScalingParams());
        rotation.dispose();
    }

    private @NotNull ScalingParams getScalingParams() throws InvalidArgumentException, UnsupportedOperatingSystemException {
        ScalingParams scalingParams = new ScalingParams();
        scalingParams.setSize(false, String.valueOf(800), String.valueOf(600), "yuv420p");
        scalingParams.setSwsFlags(new Bicubic(2, 2));
        scalingParams.setInputColorMatrix("auto");
        scalingParams.setOutColorMatrix("bt709");
        scalingParams.setInputRange("auto");
        scalingParams.setOutputRange("auto");
        scalingParams.setEval("init");
        scalingParams.setInterl("0");
        scalingParams.forceOriginalAspectRatio("disable");
        scalingParams.setDivisibleBy(2);
        return scalingParams;
    }

}
