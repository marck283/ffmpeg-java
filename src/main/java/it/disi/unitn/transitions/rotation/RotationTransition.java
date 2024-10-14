package it.disi.unitn.transitions.rotation;

import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.RotationFailedException;
import it.disi.unitn.lasagna.File;
import it.disi.unitn.videocreator.TracksMerger;
import it.disi.unitn.videocreator.filtergraph.VideoFilterGraph;
import it.disi.unitn.videocreator.filtergraph.filterchain.VideoSimpleFilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.Scale;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.ScalingParams;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RotationTransition {

    private final Rotation rotation;

    private final String tempOutDir, videoOutDir, fname;

    public RotationTransition(@NotNull String inputFile, @NotNull String tempOutDir, @NotNull String videoOutDir,
                              @NotNull String fname)
            throws IOException {
        rotation = new Rotation(inputFile, tempOutDir);
        this.tempOutDir = tempOutDir;
        this.videoOutDir = videoOutDir;
        this.fname = fname;
    }

    public void rotate(int anchorx, int anchory, double angle, @NotNull String text, @NotNull String name,
                       @NotNull Color color) throws RotationFailedException, IOException, InvalidArgumentException {
        rotation.rotate(anchorx, anchory, angle, text, name, fname, color);
    }

    private void createVideo(@NotNull FFMpegBuilder builder, long timeout, @NotNull TimeUnit tu,
                             @NotNull String outfile, @NotNull String fileExt, @NotNull ScalingParams scalingParams)
            throws Exception {
        File outDirPath = new File(tempOutDir);
        List<String> pathList = outDirPath.getFileList();
        TracksMerger merger = builder.newTracksMerger(videoOutDir + "/" + outfile + "." + fileExt);

        for(String path: pathList) {
            merger.addInput(path.replace('\\', '/'));
        }

        Scale scale = new Scale(scalingParams);
        scale.updateMap();

        VideoFilterGraph vsfg = new VideoFilterGraph();
        VideoSimpleFilterChain vsfc = new VideoSimpleFilterChain();
        vsfc.addFilter(scale);
        vsfg.addFilterChain(vsfc);
        merger.setVideoSimpleFilterGraph(vsfg);

        Path p = Paths.get("inputFile.txt").toAbsolutePath();
        merger.mergeVideos(timeout, tu, pathList, p.toString(), "./");
    }

    public void performRotation(long timeout, @NotNull TimeUnit tu, @NotNull String outfile,
                                @NotNull String fileExt, @NotNull ScalingParams scalingParams)
            throws Exception {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");
        createVideo(builder, timeout, tu, outfile, fileExt, scalingParams);
    }

    public void dispose() throws IOException {
        rotation.dispose();
        File dir = new File(tempOutDir);
        dir.removeSelf();
    }

}
