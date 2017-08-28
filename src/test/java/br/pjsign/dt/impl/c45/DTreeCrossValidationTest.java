package br.pjsign.dt.impl.c45;

import br.pjsign.dt.Instance;
import br.pjsign.dt.io.InputData;
import br.pjsign.dt.io.OutPrintTree;
import jdk.internal.util.xml.impl.Input;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DTreeCrossValidationTest {

    private InputData trainInput;
    private InputData testInput;
    private DTreeCrossValidation tree;

    private InputData trainRoboInput;

    @Before
    public void setUp() throws Exception {
        this.trainInput = new InputData("src/main/resources/trainRobocode2.arff");
        this.testInput = new InputData("src/main/resources/testProdSelection.arff");

        this.trainRoboInput = new InputData("src/main/resources/trainRobocode2.arff");

        //new OutPrintTree().printTree(this.tree.getRoot(), -1);
    }

    @Test
    public void shuffle() throws Exception {
        final int k = 10;
        this.tree = new DTreeCrossValidation(trainInput);

        final List<Instance> instances = trainInput.getInstanceSet();
        int length = instances.size();

        final List<List<Instance>> bundleList = this.tree.shuffle(instances, k);
        int n = 0;
        for (List<Instance> ins :bundleList) {
            n += ins.size();
            //System.out.println("size: " + ins.size());
            //System.out.print("[");
            //for (Instance i : ins) {
            //    System.out.print(i.getInstanceIndex() + ", ");
            //}
            //System.out.println("]");
        }
        assertEquals(k, bundleList.size());
        assertEquals(n, length);
    }

    @Test
    public void crossValidation() throws Exception {
        final int k = 10;
        this.tree = new DTreeCrossValidation(trainInput);

        final List<Instance> instances = trainInput.getInstanceSet();
        final List<Instance> instances2 = new ArrayList<Instance>(trainInput.getInstanceSet());

        final List<Double> res = this.tree.validate(instances, k);
        final List<Double> res2 = this.tree.validate(instances2, k);
        double cross = 0;
        for(Double d: res) {
            cross += d;
            System.out.println(d + ", ");
        }

        System.out.print("");
        System.out.println("Cross Validation Accuracy: " + (cross/k));

        assertEquals(0, instances.size());
        assertEquals(k, res.size());

        System.out.println("Tree: ");
        System.out.println(OutPrintTree.print(this.tree));
    }
}