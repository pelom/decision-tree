package br.pjsign.dt.impl;

import br.pjsign.dt.*;
import br.pjsign.dt.c45.InfoGainContinuous;
import br.pjsign.dt.c45.InfoGainDiscrete;
import br.pjsign.dt.io.InputData;

import java.io.IOException;
import java.util.*;

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

        this.rootNode = null;
        this.rootNode = constructTree(instances, 0);

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
            final Node leaf = createNodeLeaf(entropy, instances);
            leaf.setDepth(depth);
            return leaf;
        }

        // Choose the root attribute
        final InfoGain bestInfoGain = createInfoCalc(this.attributes).calc(this.attributeTarget, instances);

        if(bestInfoGain.getInfoGain() <= 0.01) {
            String leafLabel = getMajorityLabel(this.attributeTarget, instances);
            Node leaf = new NodeDefault(leafLabel);
            leaf.setDepth(depth);
            return leaf;
        }

        final Attribute rootAttr = bestInfoGain.getAttribute();
        // Remove the chosen attribute from attribute set
        this.attributes.remove(rootAttr);

        // Make a new root
        final Node root = createNodeRoot(rootAttr);
        root.setDepth(depth);
        // Get value subsets of the root attribute to construct branches
        final Map<String, List<Instance>> valueSubsets = bestInfoGain.getSubset();

        for (String valueName : valueSubsets.keySet()) {
            final List<Instance> subset = valueSubsets.get(valueName);
            Node child = null;

            if (isStopSubset(subset)) {
                child = createNodeLeaf(instances);
            } else {
                child = constructTree(subset, depth+1);
            }
            child.setDepth(depth+1);
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

    protected Node createNodeLeaf(List<Instance> instances)
            throws IOException {
        return this.createNodeLeaf(MAJORITY_LABEL, instances);
    }

    protected Node createNodeLeaf(double entropy, List<Instance> instances)
            throws IOException {
        String leafLabel = null;
        if (isStopEntropy(entropy)) {
            final Instance ins = instances.get(0);
            leafLabel = ins.getAttribute(this.attributeTarget.getName());
        } else {
            leafLabel = getMajorityLabel(this.attributeTarget, instances);
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

    public int lengthNodes() {
        return this.nodeList.size();
    }

    public int lengthNodesLeft() {
        return this.nodeLeafList.size();
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

                //Map<String, InfoGain> infoGainMap = new HashMap<String, InfoGain>();
                List<InfoGain> infoGainList = new ArrayList<InfoGain>(attributeList.size());
                // Iterate to find the attribute with the largest information gain
                for (final Attribute currAttribute : attributeList) {
                    final InfoGain currInfoGain = createInfoGain(currAttribute).calc(target, instances);
                    infoGainList.add(currInfoGain);
                    //infoGainMap.put(currAttribute.getName(), currInfoGain);
                    if (infoGain == null) {
                        infoGain = currInfoGain;
                    } else if(currInfoGain.getInfoGain() > infoGain.getInfoGain()) {
                        infoGain = currInfoGain;
                    }
                }
                Collections.sort(infoGainList, new Comparator<InfoGain>() {
                    public int compare(InfoGain o1, InfoGain o2) {
                        final double diff = o2.getInfoGain() - o1.getInfoGain();
                        if (diff > 0) return 1;
                        else if (diff < 0) return -1;
                        else return 0;
                    }
                });
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
        node.onTouched(test);

        if(node.isLeaf()) {
            return node;
        }
        final String value = node.getValue(test);
        final Node nodeChild = node.isTestValue(value);
//        if (attribute.isContinuuousType()) {
//            nodeChild = node.isTestValue(value);
//        } else {
//            nodeChild = node.getChildren().get(value);
//        }
        return mineInstance(test, nodeChild);
    }
}