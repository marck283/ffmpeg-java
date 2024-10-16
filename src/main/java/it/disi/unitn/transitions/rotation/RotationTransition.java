package it.disi.unitn.transitions.rotation;

import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.lasagna.MyFile;
import it.disi.unitn.videocreator.TracksMerger;
import it.disi.unitn.videocreator.filtergraph.VideoFilterGraph;
import it.disi.unitn.videocreator.filtergraph.filterchain.VideoSimpleFilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.Scale;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.scalingalgs.ScalingAlgorithm;
import org.jetbrains.annotations.NotNull;

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

    private void createVideo(@NotNull FFMpegBuilder builder, long timeout, @NotNull TimeUnit tu,
                             @NotNull String outfile, @NotNull String fileExt, @NotNull ScalingAlgorithm sws_flags,
                             @NotNull String eval, @NotNull String interl,
                             @NotNull String width, @NotNull String height, @NotNull String videoSizeID,
                             @NotNull String in_range, @NotNull String out_range, @NotNull String force_original_aspect_ratio,
                             @NotNull String inColMatrix, @NotNull String outColorMatrix, int force_divisible_by)
            throws Exception {
        MyFile outDirPath = new MyFile(tempOutDir);
        List<String> pathList = outDirPath.getFileList();
        TracksMerger merger = builder.newTracksMerger(videoOutDir + "/" + outfile + "." + fileExt);

        for(String path: pathList) {
            merger.addInput(path.replace('\\', '/'));
        }

        Scale scale = new Scale(sws_flags, eval, interl, width, height, videoSizeID, in_range, out_range, force_original_aspect_ratio,
                inColMatrix, outColorMatrix, force_divisible_by);
        scale.updateMap();

        VideoFilterGraph vsfg = new VideoFilterGraph();
        VideoSimpleFilterChain vsfc = new VideoSimpleFilterChain();
        vsfc.addFilter(scale);
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
    public void performRotation(long timeout, @NotNull TimeUnit tu, @NotNull String outfile,
                                @NotNull String fileExt, @NotNull ScalingAlgorithm sws_flags, @NotNull String eval,
                                @NotNull String interl, @NotNull String width, @NotNull String height,
                                @NotNull String videoSizeID, @NotNull String in_range, @NotNull String out_range,
                                @NotNull String force_original_aspect_ratio, @NotNull String inColMatrix,
                                @NotNull String outColorMatrix, int force_divisible_by) throws Exception {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");
        createVideo(builder, timeout, tu, outfile, fileExt, sws_flags, eval, interl, width, height, videoSizeID, in_range,
                out_range, force_original_aspect_ratio, inColMatrix, outColorMatrix, force_divisible_by);
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
