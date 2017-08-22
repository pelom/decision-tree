package br.pjsign.dt.c45;

import br.pjsign.dt.io.InputData;
import br.pjsign.dt.io.OutPrintTree;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CrossPruningTest {
    private InputData trainInput;
    private InputData trainInput2;
    private InputData testInput;
    private DTreeCrossValidation treeCross;
    private DTreePruning treePruning;

    @Before
    public void setUp() throws Exception {
        this.trainInput = new InputData("src/main/resources/trainProdSelection.arff");
        this.trainInput2 = new InputData("src/main/resources/trainProdSelection.arff");

        this.testInput = new InputData("src/main/resources/testProdSelection.arff");
        //new OutPrintTree().printTree(this.tree.getRoot(), -1);
    }

    @Test
    public void crossValidationPruning() throws Exception {
        int k = 10;
        this.treeCross = new DTreeCrossValidation(trainInput);
        this.treePruning = new DTreePruning(trainInput2);

        final List<Double> resultCross =
                this.treeCross.validate(trainInput.getInstanceSet(), k);
        //new OutPrintTree().printTree(this.treeCross.getRoot(), -1);
        final List<Double> resultPruning =
                this.treePruning.validate(trainInput2.getInstanceSet(), k);
        //new OutPrintTree().printTree(this.treePruning.getRoot(), -1);

        System.out.println("Cross: " );
        for(Double d: resultCross) {
            System.out.println(d + ", ");
        }
        System.out.println("");
        System.out.println("Pruning: " );
        for(Double d: resultPruning) {
            System.out.println(d + ", ");
        }
        System.out.println("");
        double rCross = 0;
        double rPruning = 0;
        for(int i = 0; i < resultCross.size(); i++) {
            rCross += resultCross.get(i);
            rPruning += resultPruning.get(i);
        }
        System.out.println("**********************");
        System.out.println("Cross Accuracy: " + rCross / k);
        System.out.println("Pruning Accuracy: " +  rPruning / k);
        System.out.println("**********************");
    }
}
