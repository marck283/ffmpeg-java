package it.disi.unitn.videocreator.filtergraph.filterchain.filters;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Filter {

    private final String filterName;

    protected Map<String, String> options;

    protected Filter(@NotNull String filterName) {
        this.filterName = filterName;
        options = new TreeMap<>();
    }

    public String toString() {
        List<String> helpList = new ArrayList<>();
        for(Map.Entry<String, String> e: options.entrySet()) {
            helpList.add(e.getKey() + "=\"" + e.getValue() + "\"");
        }
        return String.join("=", filterName, String.join(":", helpList));
    }
}
