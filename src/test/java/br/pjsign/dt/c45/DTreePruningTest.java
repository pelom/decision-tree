package br.pjsign.dt.c45;

import br.pjsign.dt.Instance;
import br.pjsign.dt.io.InputData;
import br.pjsign.dt.io.OutPrintTree;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DTreePruningTest {

    private InputData trainInput;
    private InputData testInput;
    private DTreePruning tree;

    @Before
    public void setUp() throws Exception {
        this.trainInput = new InputData("src/main/resources/trainProdSelection.arff");
        this.testInput = new InputData("src/main/resources/testProdSelection.arff");
    }

    @Test
    public void validate() throws Exception {
        final int k = 10;
        this.tree = new DTreePruning(trainInput);
        final List<Instance> instances = trainInput.getInstanceSet();
        final List<Double> res = this.tree.validate(instances, k);

        double cross = 0;
        for(Double d: res) {
            cross += d;
            System.out.println(d + ", ");
        }


        assertEquals(0, instances.size());
        assertEquals(k, res.size());

        System.out.print("");
        System.out.println("Pruning Validation Accuracy: " + (cross/k));

        System.out.println("PruningTree: ");
        System.out.println(OutPrintTree.print(this.tree.getRoot()));
    }

}