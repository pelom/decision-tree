package br.pjsign.dt.impl;

import br.pjsign.dt.*;
import br.pjsign.dt.c45.InfoGainContinuous;
import br.pjsign.dt.c45.InfoGainDiscrete;
import br.pjsign.dt.io.InputData;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DTreeDefault implements DTree {
    private static final int MAJORITY_LABEL = -1;

    protected List<Attribute> attributes;
    protected Attribute attributeTarget;
    protected Node rootNode;

    protected List<Node> nodeList;
    protected List<Node> nodeLeafList;
    protected List<Node> nodeDiscreteList;
    protected List<Node> nodeContinuousList;

    /**
     *
     */
    protected DTreeDefault() {
        this(null, null);
    }

    /**
     * @param input
     * @throws IOException
     */
    protected DTreeDefault(final InputData input) throws IOException {
        if(!input.isRead()) {
            input.load();
        }

        this.attributes = input.getAttributeSet();
        this.attributeTarget = input.getTargetAttribute();

        this.construct(input.getInstanceSet());
    }

    /**
     * @param attributes
     * @param attributeTarget
     */
    public DTreeDefault(final List<Attribute> attributes, final Attribute attributeTarget) {
        this.attributes = attributes;
        this.attributeTarget = attributeTarget;
    }

    public Node getRoot() {
        return rootNode;
    }

    public Node construct(final List<Instance> instances) throws IOException {
        this.nodeList = new LinkedList<Node>();
        this.nodeLeafList = new LinkedList<Node>();
        this.nodeDiscreteList = new LinkedList<Node>();
        this.nodeContinuousList = new LinkedList<Node>();

        this.rootNode = constructTree(instances, 1);

        addNodeRoot(this.rootNode);

        return this.rootNode;
    }

    private Node constructTree(List<Instance> instances, int depth) throws IOException {
		/*
		 *  Stop when (1) entropy is zero
		 *  (2) no attribute left
		 */
        final double entropy = Entropy.calculate(this.attributeTarget, instances);
        if(isStop(entropy, this.attributes)) {
            return createNodeLeaf(entropy, this.attributeTarget, instances);
        }

        // Choose the root attribute
        final InfoGain bestInfoGain = createInfoCalc(this.attributes).calc(this.attributeTarget, instances);
        final Attribute rootAttr = bestInfoGain.getAttribute();
        // Remove the chosen attribute from attribute set
        this.attributes.remove(rootAttr);

        // Make a new root
        final Node root = createNodeRoot(rootAttr);

        // Get value subsets of the root attribute to construct branches
        final Map<String, List<Instance>> valueSubsets = bestInfoGain.getSubset();

        for (String valueName : valueSubsets.keySet()) {
            final List<Instance> subset = valueSubsets.get(valueName);
            Node child = null;

            if (isStopSubset(subset)) {
                child = createNodeLeaf(this.attributeTarget, instances);
            } else {
                child = constructTree(subset, ++depth);
            }
            child.setDepth(depth);
            addNodeChild(valueName, root, child);
        }

        // Remember to add it again!
        this.attributes.add(rootAttr);

        return root;
    }

    private Boolean isStop(final double entropy, final List<Attribute> attrs) {
        return (isStopEntropy(entropy) || attrs.size() == 0);
    }

    private Boolean isStopEntropy(double entropy) {
        return entropy == 0;
    }

    private Boolean isStopSubset(List<Instance> subset) {
        return subset.size() == 0;
    }

    protected Node createNodeRoot(final Attribute rootAttr) {
        return new NodeDefault(rootAttr);
    }

    protected Node createNodeLeaf(Attribute target, List<Instance> instances)
            throws IOException {
        return this.createNodeLeaf(MAJORITY_LABEL, target, instances);
    }

    protected Node createNodeLeaf(double entropy, Attribute target, List<Instance> instances)
            throws IOException {
        String leafLabel = null;
        if (isStopEntropy(entropy)) {
            final Instance ins = instances.get(0);
            leafLabel = ins.getAttribute(target.getName());
        } else {
            leafLabel = getMajorityLabel(target, instances);
        }
        Node leaf = new NodeDefault(leafLabel);
        return leaf;
    }

    protected void addNodeRoot(Node root) {
        this.nodeList.add(root);

        if(root.getAttribute().isContinuuousType()) {
            this.nodeContinuousList.add(root);
        } else {
            this.nodeDiscreteList.add(root);
        }
    }

    protected void addNodeChild(String valueName, Node root, Node child) {
        root.addChild(valueName, child);
        this.nodeList.add(child);

        if(child.isLeaf()) {
            this.nodeLeafList.add(child);
        }

        if(child.getAttribute() != null && child.getAttribute().isContinuuousType()) {
            this.nodeContinuousList.add(child);
        } else if(child.isRoot()) {
            this.nodeDiscreteList.add(child);
        }
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public List<Node> getNodeLeafList() {
        return nodeLeafList;
    }

    public List<Node> getNodeDiscreteList() {
        return nodeDiscreteList;
    }

    public List<Node> getNodeContinuousList() {
        return nodeContinuousList;
    }

    /**
     * Get the majority target class label from instances
     * @param target
     * @param instances
     * @return String
     * @throws IOException
     */
    private String getMajorityLabel(final Attribute target, final List<Instance> instances)
            throws IOException {
        final Map<String, Integer> countValueOfTarget =
                Entropy.countValueOfTarget(target, instances);

        String maxLabel = "";
        int maxCount = 0;
        for (String s : countValueOfTarget.keySet()) {
            int currCount = countValueOfTarget.get(s);
            if (currCount > maxCount) {
                maxCount = currCount;
                maxLabel = s;
            }
        }
        return maxLabel;
    }

    protected InfoCalculation createInfoCalc(final List<Attribute> attributeList) {
        final InfoCalculation bestInfoGain = new InfoCalculation() {
            public InfoGain calc(Attribute target, List<Instance> instances) throws IOException {
                InfoGain infoGain = null;
                // Iterate to find the attribute with the largest information gain
                for (final Attribute currAttribute : attributeList) {
                    final InfoGain currInfoGain = createInfoGain(currAttribute).calc(target, instances);
                    if (infoGain == null) {
                        infoGain = currInfoGain;
                    } else if(currInfoGain.getInfoGain() > infoGain.getInfoGain()) {
                        infoGain = currInfoGain;
                    }
                }
                return infoGain;
            }

            private InfoCalculation createInfoGain(final Attribute attribute) {
                if (attribute.isContinuuousType()) {
                    return new InfoGainContinuous(attribute);
                }
                return new InfoGainDiscrete(attribute);
            }
        };
        return bestInfoGain;
    }

    public Map<Integer, Node> mineInstances(final List<Instance> testInstances) {
        final Map<Integer, Node> resultMap = new HashMap<Integer, Node>(testInstances.size());
        for (int i = 0; i < testInstances.size(); i++) {
            final Instance currInstance = testInstances.get(i);
            final Node node = mineInstance(currInstance, getRoot());
            resultMap.put(currInstance.getInstanceIndex(), node);
        }
        return resultMap;
    }

    public Node mineInstance(final Instance test) {
        return mineInstance(test, getRoot());
    }

    public Node mineInstance(final Instance test, final Node node) {
        if(node.isLeaf()) {
            return node;
        }
        final Attribute attribute = node.getAttribute();
        final String value = test.getAttribute(attribute.getName());

        Node nodeChild = null;
        if (attribute.isContinuuousType()) {
            nodeChild = node.isTestValue(value);
        } else {
            nodeChild = node.getChildren().get(value);
        }
        return mineInstance(test, nodeChild);
    }
}