package it.disi.unitn.transition.rotation;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.MultiLanguageUnsupportedOperationException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import it.disi.unitn.lasagna.MyFile;
import it.disi.unitn.transitions.t2d.rotation.RotationTransition;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.ScalingParams;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.scalingalgs.Bicubic;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class RotationTest {

    private static String tempOutDir, videoOutDir, tempVideoDir;

    @BeforeAll
    static void setUpBeforeClass() {
        tempOutDir = "./src/test/resources/output/rotation/images";
        videoOutDir = "./src/test/resources/output/rotation/video";
        tempVideoDir = "./src/test/resources/output/rotation/tempVideo";
    }

    @Test
    public void test() throws Exception {
        MyFile.makeDirs(tempOutDir);
        MyFile.makeDirs(videoOutDir);
        MyFile.makeDirs(tempVideoDir);

        StringExt str = new StringExt(String.valueOf(0));
        str.padStart(3);
        String inputExt = "jpeg";
        RotationTransition rotation = new RotationTransition("./src/test/resources/input/images/"
                + str.getVal() + "." + inputExt, videoOutDir, tempOutDir, tempVideoDir, "output", "mp4", inputExt,
                60);

        ScalingParams scPars = getScalingParams();
        rotation.setScale(scPars.getAlgorithm(),
                scPars.getEval(), scPars.getInterl(), scPars.getWidth(), scPars.getHeight(), scPars.getVideoSizeID(),
                scPars.getInputRange(), scPars.getOutputRange(), scPars.getForceOriginalAspectRatio(), scPars.getInputColorMatrix(),
                scPars.getOutputColorMatrix(), scPars.getDivisibleBy());
        rotation.setFPS("60*pal/pal", 0, null, null);
        rotation.setTransitionSpeed("0.5*PTS");

        rotation.rotate(300, 300, 100F, 350F, "test","Arial Unicode MS", Font.PLAIN,
                200, Color.BLACK);

        rotation.performTransition(1L, TimeUnit.MINUTES, true, false);
        rotation.dispose();
    }

    @Test
    public void test1() throws Exception {
        MyFile.makeDirs(tempOutDir);
        MyFile.makeDirs(videoOutDir);
        MyFile.makeDirs(tempVideoDir);

        StringExt str = new StringExt(String.valueOf(0));
        str.padStart(3);
        String inputExt = "jpeg";
        RotationTransition rotation = new RotationTransition("./src/test/resources/input/images/"
                + str.getVal() + "." + inputExt, videoOutDir, tempOutDir, tempVideoDir, "output1","mp4",
                inputExt,60);

        ScalingParams scPars = getScalingParams();
        rotation.setScale(scPars.getAlgorithm(),
                scPars.getEval(), scPars.getInterl(), scPars.getWidth(), scPars.getHeight(), scPars.getVideoSizeID(),
                scPars.getInputRange(), scPars.getOutputRange(), scPars.getForceOriginalAspectRatio(), scPars.getInputColorMatrix(),
                scPars.getOutputColorMatrix(), scPars.getDivisibleBy());
        rotation.setFPS("60*pal/pal", 0, null, null);
        rotation.setTransitionSpeed("0.5*PTS");

        rotation.rotate(300, 300, 200F, 700F, "test","Arial Unicode MS", Font.PLAIN,
                200, Color.BLACK);

        rotation.performTransition(1L, TimeUnit.MINUTES,true, false);
        rotation.dispose();
    }

    @Test
    public void test2() throws Exception {
        MyFile.makeDirs(tempOutDir);
        MyFile.makeDirs(videoOutDir);
        MyFile.makeDirs(tempVideoDir);

        StringExt str = new StringExt(String.valueOf(0));
        str.padStart(3);
        String inputExt = "jpeg";
        RotationTransition rotation = new RotationTransition("./src/test/resources/input/images/"
                + str.getVal() + "." + inputExt, videoOutDir, tempOutDir, tempVideoDir, "output2", "mp4",
                inputExt,105);

        ScalingParams scPars = getScalingParams();
        rotation.setScale(scPars.getAlgorithm(),
                scPars.getEval(), scPars.getInterl(), scPars.getWidth(), scPars.getHeight(), scPars.getVideoSizeID(),
                scPars.getInputRange(), scPars.getOutputRange(), scPars.getForceOriginalAspectRatio(), scPars.getInputColorMatrix(),
                scPars.getOutputColorMatrix(), scPars.getDivisibleBy());
        rotation.setFPS("60*pal/pal", 0, null, null);
        rotation.setTransitionSpeed("0.5*PTS");

        rotation.rotate(300, 300, 200F, 700F, "test","Arial Unicode MS", Font.PLAIN,
                200, Color.BLACK);

        rotation.performTransition(1L, TimeUnit.MINUTES,true, false);
        rotation.dispose();
    }

    @Test
    public void test3() throws Exception {
        MyFile.makeDirs(tempOutDir);
        MyFile.makeDirs(videoOutDir);
        MyFile.makeDirs(tempVideoDir);

        StringExt str = new StringExt(String.valueOf(0));
        str.padStart(3);
        String inputExt = "jpeg";
        RotationTransition rotation = new RotationTransition("./src/test/resources/input/images/"
                + str.getVal() + "." + inputExt, videoOutDir, tempOutDir, tempVideoDir, "output3", "mp4",
                inputExt,-105);

        ScalingParams scPars = getScalingParams();
        rotation.setScale(scPars.getAlgorithm(),
                scPars.getEval(), scPars.getInterl(), scPars.getWidth(), scPars.getHeight(), scPars.getVideoSizeID(),
                scPars.getInputRange(), scPars.getOutputRange(), scPars.getForceOriginalAspectRatio(), scPars.getInputColorMatrix(),
                scPars.getOutputColorMatrix(), scPars.getDivisibleBy());
        rotation.setFPS("60*pal/pal", 0, null, null);
        rotation.setTransitionSpeed("0.5*PTS");

        rotation.rotate(300, 300, 200F, 700F, "test","Arial Unicode MS", Font.PLAIN,
                200, Color.BLACK);

        rotation.performTransition(1L, TimeUnit.MINUTES,true, false);
        rotation.dispose();
    }

    private @NotNull ScalingParams getScalingParams() throws InvalidArgumentException, UnsupportedOperatingSystemException,
            MultiLanguageUnsupportedOperationException {
        ScalingParams scalingParams = new ScalingParams();
        scalingParams.setSize(false, String.valueOf(2048), String.valueOf(1024),"yuv420p");
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
