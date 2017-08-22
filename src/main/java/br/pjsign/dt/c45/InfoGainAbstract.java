package br.pjsign.dt.c45;

import br.pjsign.dt.Attribute;
import br.pjsign.dt.InfoGain;
import br.pjsign.dt.Instance;

import java.util.List;
import java.util.Map;

public abstract class InfoGainAbstract implements InfoGain {

    protected Attribute attribute;
    protected double infoGain;
    protected Map<String, List<Instance> > subset;
    protected double threshold;

    public InfoGainAbstract(final Attribute attribute) {
        this.attribute = attribute;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public double getInfoGain() {
        return infoGain;
    }

    public double getThreshold() {
        return threshold;
    }

    public Map<String, List<Instance>> getSubset() {
        return subset;
    }

    public String toString() {
        return "Attribute: " + attribute + "\n"
                + "InfoGain: " + infoGain + "\n" + "Subset: " + subset;
    }
}