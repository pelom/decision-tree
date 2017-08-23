package br.pjsign.dt.c45;

import br.pjsign.dt.Attribute;
import br.pjsign.dt.Instance;
import br.pjsign.dt.Node;
import br.pjsign.dt.Threshold;
import br.pjsign.dt.io.InputData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DTreePruning extends DTreeCrossValidation {
    private static final Logger log = LoggerFactory.getLogger(DTreePruning.class);

    /**
     * @param trainInput
     * @throws IOException
     */
    public DTreePruning(InputData trainInput) throws IOException {
        super(trainInput);
    }

    /**
     * Do cross validation on input data, which uses tree after pruning.
     * @param crossValidationN
     * @return ArrayList<Double>
     * @throws IOException
     */
    public List<Double> validate(final List<Instance> instances, int crossValidationN) throws IOException {
        log.info("validate() instances: " + instances.size() + " k: " + crossValidationN);

        final List<List<Instance>> testBundles = shuffle(instances, crossValidationN);

        log.debug("testBundles[0]: [" + testBundles.size() + ", " + testBundles.get(0).size() + "]");
        log.debug("testBundles[N-1]: [" + testBundles.size() + ", " + testBundles.get(testBundles.size()-1).size() + "]");

        final List<Double> scores = new ArrayList<Double>();
        final List<Instance> pruningInstances = new ArrayList<Instance>();

        int acc = 0;
        int tot = 0;
        for(int i = 0; i < testBundles.size(); i++) {
            final CaseTest caseTest = createCase(i, testBundles);
            int preSum = caseTest.train.size() * 2 / 3;
            int index = 0;

            final List<Instance> train66 = new ArrayList<Instance>(preSum);
            for(index = 0; index < preSum; index++) {
                train66.add(caseTest.train.get(index));
            }
            for(; index < caseTest.train.size(); index++) {
                pruningInstances.add(caseTest.train.get(index));
            }

            log.debug("caseTest.train: " + caseTest.train.size() + " caseTest.test:" + caseTest.test.size());
            log.debug("train66: " + train66.size() + " pruningInstances:" + pruningInstances.size());

            construct(train66);
            final Map<Integer, Node> pruningResultMap = mineInstances(pruningInstances);

            final PruningTree pruningTree = new PruningTree(pruningInstances, pruningResultMap);
            this.rootNode = pruningTree.execute(getRoot(), pruningInstances);

            final int correct = countCorrect(caseTest.test);
            scores.add(correct * 1.0 / caseTest.test.size());
            acc += correct;
            tot += caseTest.test.size();
        }
        log.debug("acc: " + acc);
        log.debug("err: " + (tot-acc));
        log.debug("tot: " + tot);
        return scores;
    }

    private class PruningTree {
        private List<Instance> pruningInstances;
        private Map<Integer, Node> pruningResultMap;

        public PruningTree(List<Instance> pruningInstances, Map<Integer, Node> pruningResultMap) {
            this.pruningInstances = pruningInstances;
            this.pruningResultMap = pruningResultMap;
        }

        public Node execute(final Node root, List<Instance> testInstances) {
            log.debug("execute() root: " + root.getAttribute() + " testInstances: " + testInstances.size() + "\n");

            if(isStopNull(root, testInstances)) {
                log.debug("isStopNull()");
                return null;
            }

            if(root.isLeaf()) {
                log.debug("isLeaf()");
                return root;
            }

            log.debug("children: " + root.getChildren().size());
            log.debug("key: " + root.getChildren().keySet());

            for(String keyNode : root.getChildren().keySet()) {
                final Node child = root.getChildren().get(keyNode);
                final List<Instance> curInstances = testNode(keyNode, root, testInstances);
                final Node newChild = execute(child, curInstances);
                if(newChild != null) {
                    log.debug("addChild " + root.getAttribute().getName() + " put(" + keyNode + ", " + newChild + ")");
                    root.getChildren().put(keyNode, newChild);
                }
            }

            log.debug("children: " + root.getChildren().size());
            if(root.getChildren().size() != 0) {
                final Map<String, Node> children = root.getChildren();
                for(String k : children.keySet()) {
                    if(children.get(k).isRoot()) {
                        return root;
                    }
                }
            }

            final Map<String, Integer> countLabelTestMap = countLabelTestMap(testInstances);

            log.debug("countLabelTestMap: " + countLabelTestMap.size());
            log.debug("keySet: " + countLabelTestMap.keySet());
            log.debug("values: " + countLabelTestMap.values());

            int max = 0;
            String targetLabel = "";
            for(String k : countLabelTestMap.keySet()) {
                final Integer temp = countLabelTestMap.get(k);
                if(temp > max) {
                    max = temp;
                    targetLabel = k;
                }
            }

            //int preMax = countCorrect(this.pruningInstances, this.pruningResultMap);
            int preMax = countPreMax(testInstances);
            log.debug("execute() root: " + root);
            log.debug("execute() testInstances: " + testInstances.size());
            log.debug("max: " + max + " targetLabel: " + targetLabel);
            log.debug("preMax: " + preMax);
            if(preMax > max) {
                return root;
            } else {
                log.debug("Node.LEAF");

                root.setType(Node.LEAF);
                root.getChildren().clear();
                root.setTargetLabel(targetLabel);
                return root;
            }
        }

        private Boolean isStopNull(Node node, List<Instance> testInstances) {
            if(node == null || testInstances.size() == 0) {
                return true;
            }
            return false;
        }

        private List<Instance> testNode(String keyNode, Node root, List<Instance> testInstances) {
            //log.debug("attribute:" + root.getAttribute());
            log.debug("testNode() keyNode: " + keyNode + " testInstances: " + testInstances.size());

            final List<Instance> curInstances = new ArrayList<Instance>();
            for(int i = 0; i < testInstances.size(); i++) {
                final Instance testInst = testInstances.get(i);
                final Attribute attribute = root.getAttribute();
                final String value = testInst.getAttribute(attribute.getName());
                if(attribute.isContinuuousType()) {
                    if(isTestNode(keyNode, value, root)) {
                        curInstances.add(testInst);
                    }
                } else {
                    if(keyNode.equals(value)) {
                        curInstances.add(testInst);
                    }
                }
            }

            log.debug("curInstances: " + curInstances.size());

            return curInstances;
        }

        private Boolean isTestNode(String keyNode, String value, Node root) {
            final Threshold threshold = root.createThreshold(keyNode, value);
            final String partition = threshold.getKeyLabel();

            if(partition.equals(Threshold.LESS_LABEL) && threshold.isValueLess(value)) {
                return true;
            } else if(partition.equals(Threshold.MORE_LABEL) && threshold.isValueMore(value)) {
                return true;
            }
            return false;
        }

        private Map<String, Integer> countLabelTestMap(List<Instance> testInstances) {
            final Map<String, Integer> result = new HashMap<String, Integer>();
            for(int i = 0; i < testInstances.size(); i++) {
                final Instance instance = testInstances.get(i);
                String label = instance.getAttribute(attributeTarget.getName());
                if(result.containsKey(label)) {
                    result.put(label, result.get(label) + 1);
                } else {
                    result.put(label, 1);
                }
            }
            return result;
        }

        private int countPreMax(List<Instance> testInstances) {
            int preMax = 0;

            for(int i = 0; i < testInstances.size(); i++) {
                int index = testInstances.get(i).getInstanceIndex();

                for(int j = 0; j < this.pruningInstances.size(); j++) {
                    final Instance pruningInstance = this.pruningInstances.get(j);
                    if(index == pruningInstance.getInstanceIndex()) {
                        final String value = pruningInstance.getAttribute(attributeTarget.getName());
                        final Node nodeResult = this.pruningResultMap.get(pruningInstance.getInstanceIndex());

                        if(nodeResult.getTargetLabel().equals(value)) {
                            preMax++;
                        }
                    }
                }

            }
            return preMax;
        }
    }
}