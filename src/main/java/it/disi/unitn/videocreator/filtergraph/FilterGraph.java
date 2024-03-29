package it.disi.unitn.videocreator.filtergraph;

import it.disi.unitn.videocreator.filtergraph.filterchain.FilterChain;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FilterGraph {

    private final List<FilterChain> fcList;

    public FilterGraph() {
        fcList = new ArrayList<>();
    }

    public void addFilterChain(@NotNull FilterChain filterChain) {
        fcList.add(filterChain);
    }

    public void removeFilterChain(@NotNull FilterChain filterChain) {
        fcList.remove(filterChain);
    }

    @Override
    public String toString() {
        List<String> helperList = new ArrayList<>();
        for(FilterChain fc: fcList) {
            helperList.add(fc.toString());
        }
        return "-filter_graph \"" + String.join(";", helperList) + "\"";
    }
}
