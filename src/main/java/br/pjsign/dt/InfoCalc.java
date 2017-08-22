package br.pjsign.dt;

import br.pjsign.dt.Attribute;
import br.pjsign.dt.Instance;

import java.io.IOException;
import java.util.List;

public interface InfoCalc {

    InfoGain calc(Attribute target, List<Instance> instances) throws IOException;
}
