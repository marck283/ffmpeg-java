package it.disi.unitn.transitions.t2d;

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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TransitionParent {

    protected final String inputFile, tempOutDir, videoOutDir;

    private final VideoSimpleFilterChain vsfc;

    public TransitionParent(@NotNull String inputFile, @NotNull String tempOutDir, @NotNull String videoOutDir) {
        this.inputFile = inputFile;
        this.tempOutDir = tempOutDir;
        this.videoOutDir = videoOutDir;
        vsfc = new VideoSimpleFilterChain();
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
     * This method allows the program to set the transition's speed by manipulating the video's timestamps.
     * @param rotExpr The given expression. This value must not be null or empty, and it must conform to FFmpeg's
     *                setpts and asetpts filters specifications.
     * @throws InvalidArgumentException If the given value is null or empty, or if it does not conform to the given
     * specifications.
     */
    public void setTransitionSpeed(@NotNull String rotExpr) throws InvalidArgumentException {
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
     * @param isFinal Boolean parameter to check if this call to performTransition() will create the output video.
     * @throws Exception If any exception is thrown
     */
    public void performTransition(long timeout, @NotNull TimeUnit tu, @NotNull String outfile, @NotNull String fileExt,
                                   boolean isFinal) throws Exception {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");
        createVideo(builder, timeout, tu, outfile, fileExt, isFinal);
    }

}
