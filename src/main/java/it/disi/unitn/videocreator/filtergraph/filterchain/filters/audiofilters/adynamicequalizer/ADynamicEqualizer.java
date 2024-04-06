package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.adynamicequalizer;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;

public class ADynamicEqualizer extends AudioFilter {

    private int threshold = 0, dfrequency = 1000, tfrequency = 1000, attack = 20, release = 200, ratio = 1, makeup = 0,
    range = 50;

    private double dqfactor = 1.0, tqfactor = 1.0;

    private String mode = "cutbelow", dftype = "bandpass", tftype = "bell", auto = "disabled", precision = "auto";

    /**
     * This class's constructor.
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public ADynamicEqualizer() throws InvalidArgumentException {
        super("adynamicequalizer");
    }

    @Override
    public void updateMap() {

    }
}
