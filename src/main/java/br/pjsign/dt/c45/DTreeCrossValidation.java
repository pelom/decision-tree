package br.pjsign.dt.c45;

import br.pjsign.dt.DTreeValidation;
import br.pjsign.dt.Instance;
import br.pjsign.dt.Node;
import br.pjsign.dt.impl.DTreeDefault;
import br.pjsign.dt.io.InputData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DTreeCrossValidation extends DTreeDefault implements DTreeValidation {

    /**
     * @param trainInput
     * @throws IOException
     */
    public DTreeCrossValidation(final InputData trainInput) throws IOException {
        if(!trainInput.isRead()) {
            trainInput.load();
        }
        this.attributes = trainInput.getAttributeSet();
        this.attributeTarget = trainInput.getTargetAttribute();
    }

    /**
     * Do cross validation on input data.
     * @param crossValidationN
     * @return the result of cross validation
     * @throws IOException
     */
    public List<Double> validate(final List<Instance> instances, int crossValidationN) throws IOException {
        final List<Double> scores = new ArrayList<Double>();

        final List<List<Instance>> testBundles = shuffle(instances, crossValidationN);
        for(int i = 0; i < testBundles.size(); i++) {
            final CaseTest caseTest = createCase(i, testBundles);
            construct(caseTest.train);
            int correct = countCorrect(caseTest.test);
            scores.add(correct * 1.0 / caseTest.test.size());
        }
        return scores;
    }

    protected CaseTest createCase(int i, List<List<Instance>> testBundles) {
        final CaseTest caseTest = new CaseTest();
        for(int j = 0; j < testBundles.size(); j++) {
            if(j == i) {
                caseTest.test.addAll(testBundles.get(j));
            } else {
                caseTest.train.addAll(testBundles.get(j));
            }
        }
        return caseTest;
    }

    protected int countCorrect(List<Instance> testInstance) {
        final Map<Integer, Node> resultTestMap = mineInstances(testInstance);
        return countCorrect(testInstance, resultTestMap);
    }

    protected int countCorrect(List<Instance> testInstances, final Map<Integer, Node> resultTestMap) {
        int correct = 0;
        for (final Instance item : testInstances) {
            final Node nodeSelect = resultTestMap.get(item.getInstanceIndex());
            final String testLabel = nodeSelect.getTargetLabel();
            final String label = item.getAttribute(this.attributeTarget.getName());
            if(testLabel.equals(label)) {
                correct++;
            }
        }
//            for(int i = 0; i < testInstances.size(); i++) {
//                int index = testInstances.get(i).getInstanceIndex();
//
//                for(int j = 0; j < this.pruningInstances.size(); j++) {
//                    final Instance pruningInstance = this.pruningInstances.get(j);
//                    if(index == pruningInstance.getInstanceIndex()) {
//                        final String value = pruningInstance.getAttribute(attributeTarget.getName());
//                        final Node nodeResult = this.pruningResultMap.get(pruningInstance.getInstanceIndex());
//
//                        if(nodeResult.getTargetLabel().equals(value)) {
//                            preMax++;
//                        }
//                    }
//                }
//
//            }
        return correct;
    }

    /**
     * Shuffle data and put them into k bundles, preparing for cross validation on k folds.
     * @param k
     */
    public static List<List<Instance>> shuffle(final List<Instance> instances, final int k) {
        final int total_size = instances.size();
        final int average = total_size / k;
        final Random rand = new Random(total_size);

        final List< List<Instance> > testBundles = new ArrayList<List<Instance>>();
        for(int i=0; i < k - 1; i++) {
            final List<Instance> curBundle = new ArrayList<Instance>();
            for(int j = 0; j < average; j++) {
                final int curIndex = rand.nextInt(instances.size());
                curBundle.add(instances.get(curIndex));
                instances.remove(curIndex);
            }
            testBundles.add(curBundle);
        }

        final List<Instance> lastBundle = new ArrayList<Instance>();
        for(int i = 0; i < instances.size(); i++) {
            lastBundle.add(instances.get(i));
        }
        testBundles.add(lastBundle);
        instances.clear();
        return testBundles;
    }

    protected static class CaseTest {
        public List<Instance> train;
        public List<Instance> test;
        public CaseTest() {
            this.test = new ArrayList<Instance>();
            this.train = new ArrayList<Instance>();
        }
    }
}
