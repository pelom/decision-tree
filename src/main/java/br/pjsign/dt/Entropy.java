package br.pjsign.dt;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entropy {

    public static double calculateContinuous(
            Attribute target, List<Instance> instances, int index) throws IOException {
        int totalN = instances.size();
        double infoGain = Entropy.calculate(target, instances);

        int subL = index + 1;
        int subR = instances.size() - index - 1;

        double subResL = ((double) subL) / ((double) totalN) *
                calculateContinuous(target, instances, 0, index);

        double subResR = ((double) subR) / ((double) totalN) *
                calculateContinuous(target, instances, index + 1, totalN - 1);

        infoGain -= (subResL + subResR);
        return infoGain;
    }

    /**
     * Calculate entropy of instances for the target attribute.
     * Only for discrete attribute.
     * @param target
     * @param instances
     * @return double
     * @throws IOException
     */
    public static double calculate(final Attribute target, final List<Instance> instances) throws IOException {
        return calculateContinuous(target, instances, 0, instances.size()-1);
    }

    /**
     * Calculate entropy of instances for the target attribute.
     * Only for continuous attribute.
     * Reason: arguments of methods are different. The arguments of this method has start and
     * end. Such arguments can reuse instances without separating them into different arrayLists,
     * saving time and space.
     * @param target
     * @param instances
     * @param start
     * @param end
     * @return double
     * @throws IOException
     */
    public static double calculateContinuous(final Attribute target, final List<Instance> instances,
                                        final int start, final int end) throws IOException {

        final int totalN = instances.size();
        double entropy = 0;

        final Map<String, Integer> countValueOfTarget =
                countValueOfTarget(target, instances, start, end);

        final List<String> valuesOfTarget = target.getValues();

        for (final String s : valuesOfTarget) {
            int countSingleValue = countValueOfTarget.get(s);
            if (countSingleValue == 0) continue;
            if (countSingleValue == totalN) return 0;
            double pValue = ( (double) countSingleValue ) / ( (double) totalN );
            double itemRes = -pValue * (Math.log(pValue));
            entropy += itemRes;
        }
        return entropy;
    }

    public static Map<String, Integer> countValueOfTarget(
            final Attribute target, final List<Instance> instances) throws IOException {
        return countValueOfTarget(target, instances, 0, instances.size() -1);
    }

    public static Map<String, Integer> countValueOfTarget(
            final Attribute target, final List<Instance> instances,
            final int start, final int end) throws IOException {

        final Map<String, Integer> countValueOfTarget = new HashMap<String, Integer>();

        final List<String> valuesOfTarget = target.getValues();
        for (final String s : valuesOfTarget) {
            countValueOfTarget.put(s, 0);
        }
        for (int i = start; i <= end; i++) {
            //System.out.println("i:" + instances.get(i).getInstanceIndex());
            final String valueOfInstanceAtTarget = instances.get(i).getAttribute(target.getName());
            if (!countValueOfTarget.containsKey(valueOfInstanceAtTarget)) {
                throw new IOException("Invalid input data");
            }

            countValueOfTarget.put(valueOfInstanceAtTarget,
                    countValueOfTarget.get(valueOfInstanceAtTarget) + 1);
        }
        return countValueOfTarget;
    }
}