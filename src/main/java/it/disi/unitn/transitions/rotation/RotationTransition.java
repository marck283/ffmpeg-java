package it.disi.unitn.transitions.rotation;

import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.lasagna.MyFile;
import it.disi.unitn.videocreator.TracksMerger;
import it.disi.unitn.videocreator.filtergraph.VideoFilterGraph;
import it.disi.unitn.videocreator.filtergraph.filterchain.VideoSimpleFilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.multimedia.pts.SetPts;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.fps.FPS;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.Scale;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.scalingalgs.ScalingAlgorithm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class performs the rotation.
 */
public class RotationTransition {

    /**
     * Static class to build an instance of RotationTransition.
     */
    public static class RotationBuilder {

        private final String tempOutDir, videoOutDir, fname, fext, inputFile;

        private final int angle;

        /**
         * This class's constructor.
         * @param inputFile The given input file's path.
         * @param tempOutDir The temporary video output directory's path.
         * @param videoOutDir The temporary picture output directory's path.
         * @param fname The output file's extension.
         * @param fext The given picture extension.
         * @param angle The angle to be applied to the rotation.
         */
        public RotationBuilder(@NotNull String inputFile, @NotNull String tempOutDir, @NotNull String videoOutDir,
                               @NotNull String fname, @NotNull String fext, int angle) {
            this.videoOutDir = videoOutDir;
            this.tempOutDir = tempOutDir;
            this.fname = fname;
            this.fext = fext;
            this.inputFile = inputFile;
            this.angle = angle;
        }

        /**
         * Builds an instance of RotationTransition.
         * @return an instance of RotationTransition
         * @throws InvalidArgumentException If the angle given to this instance of RotationBuilder is negative or greater
         * than 999.
         */
        public RotationTransition build() throws InvalidArgumentException {
            return new RotationTransition(inputFile, videoOutDir, tempOutDir, fname, fext, angle);
        }
    }

    private Rotation rotation;

    private final String tempOutDir, videoOutDir, fname, fext, inputFile;

    private final VideoSimpleFilterChain vsfc;

    private final int angle;

    /**
     * The class's constructor. None of the given values can be null or empty strings.
     * @param inputFile The given input file's path.
     * @param videoOutDir The temporary video output directory's path.
     * @param tempOutDir The temporary picture output directory's path.
     * @param fname The output file's extension.
     * @throws InvalidArgumentException If the given angle is negative or greater than 999.
     */
    private RotationTransition(@NotNull String inputFile, @NotNull String videoOutDir, @NotNull String tempOutDir,
                              @NotNull String fname, @NotNull String fext, int angle) throws InvalidArgumentException {
        if(angle < 0 || angle > 999) {
            throw new InvalidArgumentException("The given angle cannot be less than 0 or greater than 999 degrees.",
                    "L'angolo fornito non puo' essere minore di 0 o maggiore di 999 gradi.");
        }
        this.inputFile = inputFile;
        this.videoOutDir = videoOutDir;
        this.tempOutDir = tempOutDir;
        this.fname = fname;
        this.fext = fext;
        vsfc = new VideoSimpleFilterChain();
        this.angle = angle;
    }

    /**
     * This method performs the rotation.
     * @param anchorx The x-value of the anchor.
     * @param anchory The y-value of the anchor.
     * @param text The text to be writer.
     * @param fext The given picture extension.
     * @param fontFamily The given font's family. This value cannot be null or an empty string.
     * @param fontStyle The given font's style.
     * @param fontSize The given font's size. This value cannot be less than or equal to zero.
     * @param color The chosen Color instance
     * @throws InvalidArgumentException If one of the non-null arguments is null or an empty string
     */
    public void rotate(int anchorx, int anchory, @NotNull String text, @NotNull String fext,
                       @NotNull String fontFamily, int fontStyle, int fontSize, @NotNull Color color) throws Exception {
        int j = 0;
        for(int i = 0; i < angle; i++) {
            if((i + 1)%10 == 0) {
                //Save intermediate video and delete the folder's content.
                StringExt strExt = new StringExt(String.valueOf(j));
                strExt.padStart(3);
                performRotation(1L, TimeUnit.MINUTES, strExt.getVal(), fname, false);

                MyFile tempDir = new MyFile(tempOutDir);
                tempDir.removeContent(fext);
            }

            StringExt str1 = new StringExt(String.valueOf(i));
            str1.padStart(3);

            rotation = new Rotation(inputFile, tempOutDir);
            rotation.rotate(anchorx, anchory, j + 1, text, str1.getVal(), fext, fontFamily, fontStyle, fontSize, color);

            j++;
        }
    }

    /**
     * This method sets the scaling filter's parameters.
     * @param sws_flags The chosen ScalingAlgorithm instance.
     * @param eval The "eval" parameter's expression.
     * @param interl The interlacing mode.
     * @param width The frames' width.
     * @param height The frames' height.
     * @param videoSizeID The frames' video size ID.
     * @param in_range The input color's range.
     * @param out_range The output color's range.
     * @param force_original_aspect_ratio This parameter tells the program how to force the aspect ratio. See {@link Scale}
     *                                    for more information.
     * @param inColMatrix The input color's matrix.
     * @param outColorMatrix The output color's matrix.
     * @param force_divisible_by An integer value by which the width and the height must be divisible (if set).
     * @throws InvalidArgumentException If any of the given values is null, an empty string or less than or equal to zero
     */
    public void setScale(@NotNull ScalingAlgorithm sws_flags,
                         @NotNull String eval, @NotNull String interl,
                         @NotNull String width, @NotNull String height, @NotNull String videoSizeID,
                         @NotNull String in_range, @NotNull String out_range, @NotNull String force_original_aspect_ratio,
                         @NotNull String inColMatrix, @NotNull String outColorMatrix, int force_divisible_by) throws InvalidArgumentException {
        Scale scale = new Scale(sws_flags, eval, interl, width, height, videoSizeID, in_range, out_range, force_original_aspect_ratio,
                inColMatrix, outColorMatrix, force_divisible_by);
        scale.updateMap();

        vsfc.addFilter(scale);
    }

    /**
     * This method sets the "fps"'s video filter parameters.
     * @param fpsEx The framerate expression.
     * @param start_time The starting instant.
     * @param round The "round" parameter's value.
     * @param eof_action The "eof_action" parameter's value.
     * @throws InvalidArgumentException If the given framerate expression is null or an empty string
     */
    public void setFPS(@NotNull String fpsEx, int start_time, @Nullable String round, @Nullable String eof_action)
            throws InvalidArgumentException {
        FPS fps = new FPS();
        fps.setFPS(fpsEx);
        fps.setStartTime(start_time);

        if(!StringExt.checkNullOrEmpty(round)) {
            fps.setRound(round);
        }

        if(!StringExt.checkNullOrEmpty(eof_action)) {
            fps.setEOFAction(eof_action);
        }

        fps.updateMap();

        vsfc.addFilter(fps);
    }

    /**
     * This method allows the program to set the rotation's speed by manipulating the video's timestamps.
     * @param rotExpr The given expression. This value must not be null or empty, and it must conform to FFmpeg's
     *                setpts and asetpts filters specifications.
     * @throws InvalidArgumentException If the given value is null or empty, or if it does not conform to the given
     * specifications.
     */
    public void setRotationSpeed(@NotNull String rotExpr) throws InvalidArgumentException {
        SetPts pts = new SetPts();
        pts.setExpr(rotExpr);
        pts.updateMap();

        vsfc.addFilter(pts);
    }

    private void createVideo(@NotNull FFMpegBuilder builder, long timeout, @NotNull TimeUnit tu,
                             @NotNull String outfile, @NotNull String fileExt, boolean isFinal) throws Exception {
        MyFile outDirPath = new MyFile((isFinal) ? videoOutDir : tempOutDir);
        List<String> pathList = outDirPath.getFileList();
        TracksMerger merger = builder.newTracksMerger(videoOutDir + "/" + outfile + "." + fileExt);

        Collections.sort(pathList);

        if(!vsfc.isEmpty()) {
            VideoFilterGraph vsfg = new VideoFilterGraph();
            vsfg.addFilterChain(vsfc);
            merger.setVideoSimpleFilterGraph(vsfg);
        }

        Path p = Paths.get("inputFile.txt").toAbsolutePath();
        merger.mergeVideos(timeout, tu, pathList, p.toString(), "./");
    }

    /**
     * This method allows the user to create the output video.
     * @param timeout The given timeout.
     * @param tu The given TimeUnit instance.
     * @param outfile The given output file's path.
     * @param fileExt The file's extension.
     * @param isFinal Boolean parameter to check if this call to performRotation() will create the output video.
     * @throws Exception If any exception is thrown
     */
    public void performRotation(long timeout, @NotNull TimeUnit tu, @NotNull String outfile, @NotNull String fileExt,
                                boolean isFinal) throws Exception {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");
        createVideo(builder, timeout, tu, outfile, fileExt, isFinal);
    }

    /**
     * This method disposes of the underlying objects used in this class.
     */
    public void dispose() {
        try {
            rotation.dispose();
            MyFile tempDir = new MyFile(tempOutDir);
            tempDir.removeContent(fext);

            tempDir = new MyFile(videoOutDir);
            tempDir.removeContentExceptFile("output");
        } catch(IOException | InvalidArgumentException ex) {
            ex.printStackTrace();
        }
    }

}
