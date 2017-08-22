package br.pjsign.dt.c45;

import br.pjsign.dt.Attribute;
import br.pjsign.dt.Instance;
import br.pjsign.dt.Node;
import br.pjsign.dt.impl.DecisionTreeDefault;
import br.pjsign.dt.io.InputData;

import java.io.IOException;
import java.util.*;

public class C45DecisionTree extends DecisionTreeDefault {

    /**
     * @param trainInput
     * @param testInput
     * @throws IOException
     */
    public C45DecisionTree(final InputData trainInput, final InputData testInput) throws IOException {
        super(trainInput);
        if(!testInput.isRead()) {
            testInput.load();
        }
    }

//
//    protected Threshold createThreshold(String keyChild, String value) {
//        return ThresholdContinuous.generateThreshold(keyChild, value);
//    }



}
