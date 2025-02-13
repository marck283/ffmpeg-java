package it.disi.unitn.transition.translation;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.MultiLanguageUnsupportedOperationException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import it.disi.unitn.lasagna.MyFile;
import it.disi.unitn.transitions.t2d.translation.TranslationTransition;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.ScalingParams;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.scalingalgs.Bicubic;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.concurrent.TimeUnit;

public class TranslationTest {

    @Test
    public void test() throws Exception {
        String tempOutDir = "./src/test/resources/output/translation/images", videoOutDir = "./src/test/resources/output/translation/video",
                tempVideoDir = "./src/test/resources/output/translation/tempVideo";
        MyFile.makeDirs(tempOutDir);
        MyFile.makeDirs(videoOutDir);
        MyFile.makeDirs(tempVideoDir);

        StringExt str = new StringExt(String.valueOf(0));
        str.padStart(3);
        String inputExt = "jpeg";
        TranslationTransition translation = new TranslationTransition("./src/test/resources/input/images/"
                + str.getVal() + "." + inputExt, tempOutDir, videoOutDir, tempVideoDir, "mp4",45, 95, inputExt);

        ScalingParams scPars = getScalingParams();
        translation.setScale(scPars.getAlgorithm(),
                scPars.getEval(), scPars.getInterl(), scPars.getWidth(), scPars.getHeight(), scPars.getVideoSizeID(),
                scPars.getInputRange(), scPars.getOutputRange(), scPars.getForceOriginalAspectRatio(), scPars.getInputColorMatrix(),
                scPars.getOutputColorMatrix(), scPars.getDivisibleBy());
        translation.setFPS("60*pal/pal", 0, null, null);
        translation.setTransitionSpeed("0.5*PTS");

        translation.translate(new Point2D.Float(200F, 350F), "test", "mp4","Arial Unicode MS",
                Font.PLAIN,200, Color.BLACK);

        //translation.performTransition(1L, TimeUnit.MINUTES, "last", false, true);
        translation.performTransition(1L, TimeUnit.MINUTES, "output", true, false);
        translation.dispose();
    }

    @Test
    public void test1() throws Exception {
        String tempOutDir = "./src/test/resources/output/translation/images", videoOutDir = "./src/test/resources/output/translation/video",
                tempVideoDir = "./src/test/resources/output/translation/tempVideo";
        MyFile.makeDirs(tempOutDir);
        MyFile.makeDirs(videoOutDir);
        MyFile.makeDirs(tempVideoDir);

        StringExt str = new StringExt(String.valueOf(0));
        str.padStart(3);
        String inputExt = "jpeg";
        TranslationTransition translation = new TranslationTransition("./src/test/resources/input/images/"
                + str.getVal() + "." + inputExt, tempOutDir, videoOutDir, tempVideoDir, "mp4",45, -95, inputExt);

        ScalingParams scPars = getScalingParams();
        translation.setScale(scPars.getAlgorithm(),
                scPars.getEval(), scPars.getInterl(), scPars.getWidth(), scPars.getHeight(), scPars.getVideoSizeID(),
                scPars.getInputRange(), scPars.getOutputRange(), scPars.getForceOriginalAspectRatio(), scPars.getInputColorMatrix(),
                scPars.getOutputColorMatrix(), scPars.getDivisibleBy());
        translation.setFPS("60*pal/pal", 0, null, null);
        translation.setTransitionSpeed("0.5*PTS");

        translation.translate(new Point2D.Float(500F, 850F), "test", "mp4","Arial Unicode MS",
                Font.PLAIN,200, Color.BLACK);

        //translation.performTransition(1L, TimeUnit.MINUTES, "last", false, true);
        translation.performTransition(1L, TimeUnit.MINUTES, "output1", true, false);
        translation.dispose();
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
