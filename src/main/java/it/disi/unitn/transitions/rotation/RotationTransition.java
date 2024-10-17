package it.disi.unitn.transitions.rotation;

import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.lasagna.MyFile;
import it.disi.unitn.videocreator.TracksMerger;
import it.disi.unitn.videocreator.filtergraph.VideoFilterGraph;
import it.disi.unitn.videocreator.filtergraph.filterchain.VideoSimpleFilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.fps.FPS;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.Scale;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.scalingalgs.ScalingAlgorithm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class performs the rotation.
 */
public class RotationTransition {

    private final Rotation rotation;

    private final String tempOutDir, videoOutDir, fname;

    private final VideoSimpleFilterChain vsfc;

    /**
     * The class's constructor. None of the given values can be null or empty strings.
     * @param inputFile The given input file's path.
     * @param tempOutDir The given temporary output directory's path.
     * @param videoOutDir The temporary video output directory's path.
     * @param fname The output file's name.
     */
    public RotationTransition(@NotNull String inputFile, @NotNull String tempOutDir, @NotNull String videoOutDir,
                              @NotNull String fname) {
        rotation = new Rotation(inputFile, tempOutDir);
        this.tempOutDir = tempOutDir;
        this.videoOutDir = videoOutDir;
        this.fname = fname;
        vsfc = new VideoSimpleFilterChain();
    }

    /**
     * This method performs the rotation.
     * @param anchorx The x-value of the anchor.
     * @param anchory The y-value of the anchor.
     * @param angle The angle expressed in radians.
     * @param text The text to be writer.
     * @param name The given output file's name.
     * @param color The chosen Color instance
     * @throws InvalidArgumentException If one of the non-null arguments is null or an empty string
     */
    public void rotate(int anchorx, int anchory, double angle, @NotNull String text, @NotNull String name,
                       @NotNull Color color) throws InvalidArgumentException {
        rotation.rotate(anchorx, anchory, angle, text, name, fname, color);
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

    private void createVideo(@NotNull FFMpegBuilder builder, long timeout, @NotNull TimeUnit tu,
                             @NotNull String outfile, @NotNull String fileExt) throws Exception {
        MyFile outDirPath = new MyFile(tempOutDir);
        List<String> pathList = outDirPath.getFileList();
        TracksMerger merger = builder.newTracksMerger(videoOutDir + "/" + outfile + "." + fileExt);

        for(String path: pathList) {
            merger.addInput(path.replace('\\', '/'));
        }

        VideoFilterGraph vsfg = new VideoFilterGraph();
        vsfg.addFilterChain(vsfc);
        merger.setVideoSimpleFilterGraph(vsfg);

        Path p = Paths.get("inputFile.txt").toAbsolutePath();
        merger.mergeVideos(timeout, tu, pathList, p.toString(), "./");
    }

    /**
     * This method allows the user to create the output video.
     * @param timeout The given timeout.
     * @param tu The given TimeUnit instance.
     * @param outfile The given output file's path.
     * @param fileExt The file's extension.
     * @throws Exception If any exception is thrown
     */
    public void performRotation(long timeout, @NotNull TimeUnit tu, @NotNull String outfile, @NotNull String fileExt)
            throws Exception {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");
        createVideo(builder, timeout, tu, outfile, fileExt);
    }

    /**
     * This method disposes of the underlying objects used in this class.
     * @throws IOException If an I/O error occurs
     */
    public void dispose() throws IOException {
        rotation.dispose();
        MyFile dir = new MyFile(tempOutDir);
        dir.removeSelf();
    }

}
