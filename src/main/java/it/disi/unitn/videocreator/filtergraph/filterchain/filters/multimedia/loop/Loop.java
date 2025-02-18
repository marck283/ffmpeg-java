package it.disi.unitn.videocreator.filtergraph.filterchain.filters.multimedia.loop;

import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;

/**
 * This class implements FFmpeg's "loop" multimedia filter.
 */
public class Loop extends Filter {

    private int loops, size, start, time;

    /**
     * This class's constructor. Constructs a new loop filter.
     */
    public Loop() {
        super("loop");
        loops = 0;
        size = 0;
        start = 0;
        time = 0;
    }

    public void setLoops(int loops) {
        this.loops = loops;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public void updateMap() {
        options.put("loop", String.valueOf(loops));
        options.put("size", String.valueOf(size));
        options.put("start", String.valueOf(start));
        if(start == -1) {
            options.put("time", String.valueOf(time));
        }
    }
}
