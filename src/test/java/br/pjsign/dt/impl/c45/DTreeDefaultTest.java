package br.pjsign.dt.impl.c45;

import br.pjsign.dt.Instance;
import br.pjsign.dt.Node;
import br.pjsign.dt.impl.c45.DTreeDefault;
import br.pjsign.dt.io.InputData;
import br.pjsign.dt.io.OutPrintTree;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class DTreeDefaultTest {
    private InputData trainInput;
    private InputData testInput;

    private InputData trainRoboInput;

    //private InputData trainInput2;
    //private InputData testInput2;

    private DTreeDefault tree;

    @Before
    public void setUp() throws Exception {
        //this.trainInput = new InputData("src/main/resources/weather.numeric.arff");

        this.trainInput = new InputData("src/main/resources/trainProdSelection.arff");
        this.testInput = new InputData("src/main/resources/testProdSelection.arff");

        //this.trainInput2 = new InputData("src/main/resources/trainProdIntro.binary.arff");
        //this.testInput2 = new InputData("src/main/resources/testProdIntro.real.arff");

        this.trainRoboInput = new InputData("src/main/resources/trainRobocode2.arff");
    }

    @Test
    public void test() throws Exception {
        this.tree = new DTreeDefault(this.trainRoboInput);
        final  Map<Integer, Node> result =
                this.tree.mineInstances(this.trainRoboInput.getInstanceSet());

        //this.tree.construct(this.trainInput.getInstanceSet());
        System.out.println(OutPrintTree.print(tree, true, true));
    }

    @Test
    public void construct() throws Exception {
        this.tree = new DTreeDefault(trainInput);
        //final Node rootNode = tree.construct(trainInput.getInstanceSet());
        //new OutPrintTree().printTree(rootNode, -1);
        assertEquals("Vacation", this.tree.getRoot().getAttribute().getName());
    }

    @Test
    public void nodesTree() throws Exception {
        this.tree = new DTreeDefault(this.trainInput);
        //this.tree.construct(this.trainInput.getInstanceSet());
        System.out.println(OutPrintTree.print(tree, true, true));
//        System.out.println("Nodes Continuous");
//        for (int i= tree.getNodeContinuousList().size()-1; i >= 0; i--) {
//            System.out.println(tree.getNodeContinuousList().get(i));
//        }
//        System.out.println("Nodes Discrete");
//        for (int i= tree.getNodeDiscreteList().size()-1; i >= 0; i--) {
//            System.out.println(tree.getNodeDiscreteList().get(i));
//        }
//        System.out.println("Nodes leaf");
//        for (int i= tree.getNodeLeafList().size()-1; i >= 0; i--) {
//            System.out.println(tree.getNodeLeafList().get(i));
//        }

        assertEquals(58, tree.getNodeList().size());
        assertEquals(13, tree.getNodeContinuousList().size());
        assertEquals(7, tree.getNodeDiscreteList().size());
        assertEquals(38, tree.getNodeLeafList().size());
    }

    @Test
    public void getRootNode() throws Exception {
        this.tree = new DTreeDefault(trainInput);
        final Node rootNode = tree.construct(trainInput.getInstanceSet());
        assertEquals(rootNode, tree.getRoot());
    }

    @Test
    public void mineInstance() throws Exception {
        this.tree = new DTreeDefault(trainInput);
        this.testInput.load();
        final List<Instance> testInstances = this.testInput.getInstanceSet();
        final Instance ins = testInstances.get(testInstances.size()-1);
        assertEquals("C1", ins.getAttribute("label"));
        final Node node = tree.mineInstance(ins);
        assertEquals("C5", node.getTargetLabel());
    }

    @Test
    public void mineInstances() throws Exception {
        this.tree = new DTreeDefault(trainInput);
        this.testInput.load();
        final List<Instance> testInstances = this.testInput.getInstanceSet();
        final Map<Integer, Node> instanceNodeMap = tree.mineInstances(testInstances);

        final Map<String, Integer> testResult = new HashMap<String, Integer>(instanceNodeMap.size());
        for(Node node : instanceNodeMap.values()) {
            testResult.put(node.getTargetLabel(), 0);
        }
        final List<Instance> testInstance = this.testInput.getInstanceSet();
        for (Instance ins: testInstance ) {
            final Node node = instanceNodeMap.get(ins.getInstanceIndex());
            testResult.put(node.getTargetLabel(), testResult.get(node.getTargetLabel()) +1);
        }
        assertEquals(21, instanceNodeMap.size());
        assertEquals(Integer.valueOf(4), testResult.get("C1"));
        assertEquals(Integer.valueOf(4), testResult.get("C2"));
        assertEquals(Integer.valueOf(4), testResult.get("C3"));
        assertEquals(Integer.valueOf(5), testResult.get("C4"));
        assertEquals(Integer.valueOf(4), testResult.get("C5"));

        System.out.println(OutPrintTree.print(tree));

//        System.out.println("Nodes Continuous");
//        for (int i= tree.getNodeContinuousList().size()-1; i >= 0; i--) {
//            System.out.println(tree.getNodeContinuousList().get(i));
//        }
//        System.out.println("Nodes Discrete");
//        for (int i= tree.getNodeDiscreteList().size()-1; i >= 0; i--) {
//            System.out.println(tree.getNodeDiscreteList().get(i));
//        }
//        System.out.println("Nodes leaf");
//        for (int i= tree.getNodeLeafList().size()-1; i >= 0; i--) {
//            System.out.println(tree.getNodeLeafList().get(i));
//        }

    }

}