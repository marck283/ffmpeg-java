package it.disi.unitn.transitions.rotation;

import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.RotationFailedException;
import it.disi.unitn.lasagna.File;
import it.disi.unitn.transitions.rotation.processpool.ProcessPool;
import it.disi.unitn.videocreator.TracksMerger;
import it.disi.unitn.videocreator.filtergraph.FilterGraph;
import it.disi.unitn.videocreator.filtergraph.filterchain.FilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.multimedia.concat.Concat;
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

    private void scaleEach(@NotNull List<String> pathList, int width, int height,
                           @NotNull String codec, @NotNull String fileExt)
            throws Exception {
        String temp = videoOutDir + "/temp";
        File.makeDirs(temp);
        ProcessPool pool = new ProcessPool(width, height);
        int i = 0;
        for(String path: pathList) {
            pool.execute(i, temp, fileExt, path, codec, 1L);
            i += 1;
        }

        Thread t1 = new Thread(() -> {
            try {
                pool.doWait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t1.join();
    }

    private void createVideo(@NotNull FFMpegBuilder builder, long timeout, @NotNull TimeUnit tu, int width, int height,
                             @NotNull String outfile, @NotNull String codec, @NotNull String fileExt)
            throws Exception {
        File outDirPath = new File(tempOutDir);
        List<String> pathList = outDirPath.getFileList(), pl1;

        scaleEach(pathList, width, height, codec, fileExt);

        File f1 = new File(videoOutDir + "/temp");
        pl1 = f1.getFileList();
        TracksMerger merger = builder.newTracksMerger(videoOutDir + "/" + outfile + "." + fileExt);
        Concat concat = new Concat();
        int i = 0;

        while(pl1.size() != pathList.size()) {
            pl1 = f1.getFileList();
        }
        for(String path: pl1) {
            merger.addInput(path.replace('\\', '/'));
            concat.addInput(i + ":0");
            i += 1;
        }

        concat.setN(pl1.size());
        concat.setV(1);
        concat.setA(0);
        concat.addOutput("v");
        concat.updateMap();

        FilterGraph vsfg = new FilterGraph();
        FilterChain vsfc = new FilterChain();
        vsfc.addFilter(concat);
        vsfg.addFilterChain(vsfc);
        merger.setComplexFilterGraph(vsfg);

        merger.setFrameRate(1);
        merger.setStreamToMap("[v]");

        Path p = Paths.get("inputFile.txt");
        merger.mergeVideos(timeout, tu, pl1, p.toFile().getAbsolutePath(), "./");
    }

    public void performRotation(long timeout, @NotNull TimeUnit tu, int width, int height, @NotNull String outfile,
                                @NotNull String codec, @NotNull String fileExt)
            throws Exception {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");
        createVideo(builder, timeout, tu, width, height, outfile, codec, fileExt);
    }

    public void dispose() throws IOException {
        rotation.dispose();
        File dir = new File(tempOutDir);
        dir.removeSelf();
    }

}
