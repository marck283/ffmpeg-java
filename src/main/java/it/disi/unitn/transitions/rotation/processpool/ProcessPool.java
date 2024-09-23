package it.disi.unitn.transitions.rotation.processpool;

import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import it.disi.unitn.videocreator.VideoCreator;
import it.disi.unitn.videocreator.filtergraph.FilterGraph;
import it.disi.unitn.videocreator.filtergraph.filterchain.FilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.format.Format;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.Scale;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.scalingalgs.Bicubic;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to create a thread pool which will be used to execute a specified number of threads.
 */
public class ProcessPool {

    private final int width, height;

    private final List<ExecutorResHandler> exlist;

    /**
     * This constructor creates a new thread pool which will be used to execute a specified number of threads.
     * @param width The pictures' width
     * @param height The pictures' height
     * @throws InvalidArgumentException If any of the arguments given to this constructor is null or less than or equal
     * to zero
     */
    public ProcessPool(int width,
                       int height) throws InvalidArgumentException {
        if(width <= 0 || height <= 0) {
            throw new InvalidArgumentException("No argument to this constructor can be null, an empty string  or less " +
                    "than or equal to zero.", "Nessuno degli argomenti forniti a questo costruttore puo' essere null, " +
                    "una stringa vuota o minore o uguale a zero.");
        }
        this.width = width;
        this.height = height;
        exlist = new ArrayList<>();
    }

    /**
     * This method waits until all tasks have terminated correctly.
     * @throws InterruptedException if any thread interrupted the current thread before or while the current thread was
     * waiting. The interrupted status of the current thread is cleared when this exception is thrown.
     */
    public synchronized void doWait() throws InterruptedException {
        wait();
    }

    /**
     * This method removes ExecutorResHandler instances from the list of currently active instances.
     * @param handler The ExecutorResHandler instance to be used
     */
    public synchronized void removeHandler(ExecutorResHandler handler) {
        if(exlist.size() == 1 && exlist.contains(handler)) {
            //Notifies all threads that were waiting on this object's monitor. This call needs to happen either inside a
            //synchronized method or inside a synchronized code block.
            notifyAll();
        }
        exlist.remove(handler);
    }

    /**
     * This method executes a process from a ProcessBuilder instance and keeps track of a callback to be executed at the
     * process's termination.
     * @throws InvalidArgumentException if any of the arguments to this method is null
     */
    public void execute(int i, @NotNull String videoOutDir, @NotNull String fileExt,
                        @NotNull String path, @NotNull String codec, long timeout) throws InvalidArgumentException,
            UnsupportedOperatingSystemException, IOException {
        StringExt inFile = new StringExt(String.valueOf(i));
        inFile.padStart(3);
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");
        VideoCreator creator = builder.newVideoCreator(videoOutDir + "/" + inFile.getVal() + "." +
                fileExt);
        creator.addInput(path.replace('\\', '/'));
        creator.setCodecID(codec, false);
        creator.setPixelFormat("yuv420p");

        Scale scale = new Scale();
        creator.setScaleParams(false, scale, new Bicubic(0.3333, 0.3333),
                String.valueOf(width), String.valueOf(height),
                "auto", "bt709", "auto", "auto", "init",
                "0", "disable", 2);

        Format format = creator.setFormat(new Format());

        FilterGraph vsfg = new FilterGraph();
        FilterChain vsfc = new FilterChain();
        vsfc.addAllFilters(scale, format);
        vsfg.addFilterChain(vsfc);
        creator.setComplexFilterGraph(vsfg);

        creator.setFrameRate(1);
        creator.createCommand();

        ExecutorResHandler resHandler = new ExecutorResHandler(this);
        exlist.add(resHandler);

        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(timeout, TimeUnit.MINUTES, "./", resHandler);
    }
}
