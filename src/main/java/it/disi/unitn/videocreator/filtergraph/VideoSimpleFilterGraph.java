package it.disi.unitn.videocreator.filtergraph;

import it.disi.unitn.videocreator.filtergraph.filterchain.SimpleFilterChain;

import java.util.ArrayList;
import java.util.List;

public class VideoSimpleFilterGraph extends SimpleFilterGraph {

    @Override
    public String toString() {
        List<String> helperList = new ArrayList<>();
        for(SimpleFilterChain fc: sfcList) {
            helperList.add(fc.toString());
        }
        return "-vf \"" + String.join(";", helperList) + "\"";
    }

}
