package br.pjsign.dt.impl;

import br.pjsign.dt.Instance;
import br.pjsign.dt.Node;
import br.pjsign.dt.io.InputData;
import br.pjsign.dt.io.OutPrintTree;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DTreeDefaultTest {
    private InputData trainInput;
    private InputData testInput;
    private DTreeDefault tree;

    @Before
    public void setUp() throws Exception {
        this.trainInput = new InputData("src/main/resources/trainProdSelection.arff");
        this.testInput = new InputData("src/main/resources/testProdSelection.arff");
        this.tree = new DTreeDefault(trainInput);
    }

    @Test
    public void construct() throws Exception {
        final Node rootNode = tree.construct(trainInput.getInstanceSet());

        //new OutPrintTree().printTree(rootNode, -1);
        assertEquals("Vacation", rootNode.getAttribute().getName());
    }

    @Test
    public void nodesTree() throws Exception {
        final Node rootNode = tree.construct(trainInput.getInstanceSet());
        System.out.println(tree.getNodeList().size());

        System.out.println(OutPrintTree.print(rootNode));

        System.out.println("Nodes Continuous");
        for (int i= tree.getNodeContinuousList().size()-1; i >= 0; i--) {
            System.out.println(tree.getNodeContinuousList().get(i));
        }
        System.out.println("Nodes Discrete");
        for (int i= tree.getNodeDiscreteList().size()-1; i >= 0; i--) {
            System.out.println(tree.getNodeDiscreteList().get(i));
        }
//
        System.out.println("Nodes leaf");
        for (int i= tree.getNodeLeafList().size()-1; i >= 0; i--) {
            System.out.println(tree.getNodeLeafList().get(i));
        }

        assertEquals(60, tree.getNodeList().size());
        assertEquals(14, tree.getNodeContinuousList().size());
        assertEquals(7, tree.getNodeDiscreteList().size());
        assertEquals(39, tree.getNodeLeafList().size());
    }

    @Test
    public void getRootNode() throws Exception {
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
        assertEquals(Integer.valueOf(2), testResult.get("C2"));
        assertEquals(Integer.valueOf(4), testResult.get("C3"));
        assertEquals(Integer.valueOf(6), testResult.get("C4"));
        assertEquals(Integer.valueOf(5), testResult.get("C5"));
    }

}