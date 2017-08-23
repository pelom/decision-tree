package br.pjsign.dt;

import java.io.IOException;
import java.util.List;

public interface DTreeValidation {

    List<Double> validate(final List<Instance> instances, int crossValidationN) throws IOException;
}
