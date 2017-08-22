package br.pjsign.dt;

import br.pjsign.dt.Attribute;
import br.pjsign.dt.Instance;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface InfoGain {
    Attribute getAttribute();

    double getInfoGain();

    double getThreshold();

    Map<String, List<Instance>> getSubset();

}
