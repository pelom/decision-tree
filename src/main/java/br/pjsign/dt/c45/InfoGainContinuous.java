/*********************************
 * Author: Xue (Charlotte) Lin
 * Date: 2015/04/01
 *********************************/
package br.pjsign.dt.c45;

import java.io.IOException;
import java.util.*;

import br.pjsign.dt.*;

public class InfoGainContinuous extends InfoGainAbstract implements InfoCalc {

    /**
     * @param attribute
     */
	public InfoGainContinuous(Attribute attribute) {
		super(attribute);
	}

	public InfoGain calc(final Attribute target, final List<Instance> instances) throws IOException {

		// Initialize threshold and infoGain
		// (1) Get the name of the attribute to be calculated
		final String attributeName = attribute.getName();

		// (2) Sort instances according to the attribute
		sortInstance(instances);

		/*
		 (3) Get each position that target value change,
			then calculate information gain of each position
		    find the maximum position value to be the threshold
		 */
		int thresholdPos = getIndexThredhold(target, instances);

		// (4) Calculate threshold
		final String aValue = instances.get(thresholdPos).getAttribute(attributeName);
		final String bValue = instances.get(thresholdPos).getAttribute(attributeName);
		this.threshold = (Double.parseDouble(aValue) + Double.parseDouble(bValue)) / 2;

		// Initialize subset
		this.subset = new HashMap<String, List<Instance>>();

		final List<Instance> left = new ArrayList<Instance>();
		for (int i = 0; i < thresholdPos; i++) {
			left.add(instances.get(i));
		}

		final List<Instance> right = new ArrayList<Instance>();
		for (int i = thresholdPos + 1; i < instances.size(); i++) {
			right.add(instances.get(i));
		}

		this.subset.put(ThresholdContinuous.generateLess(threshold), left);
		this.subset.put(ThresholdContinuous.generateMore(threshold), right);
		return this;
	}

	private int getIndexThredhold(final Attribute target, List<Instance> instances) throws IOException {
        int thresholdPos = 0;

        for (int i = 0; i < instances.size() - 1; i++) {
            final String instanceValue = instances.get(i).getAttribute(attribute.getName());
            final String instanceValue2 = instances.get(i + 1).getAttribute(attribute.getName());

            if (!instanceValue.equals(instanceValue2)) {
                double currInfoGain = calculateConti(target, instances, i);
                if (currInfoGain - infoGain > 0) {
                    infoGain = currInfoGain;
                    thresholdPos = i;
                }
            }
        }
        return thresholdPos;
    }

    public static double calculateConti(Attribute target, List<Instance> instances, int index) throws IOException {
        int totalN = instances.size();
        double infoGain = Entropy.calculate(target, instances);

        int subL = index + 1;
        int subR = instances.size() - index - 1;

        double subResL = ((double) subL) / ((double) totalN) *
                Entropy.calculateConti(target, instances, 0, index);

        double subResR = ((double) subR) / ((double) totalN) *
                Entropy.calculateConti(target, instances, index + 1, totalN - 1);

        infoGain -= (subResL + subResR);
        return infoGain;
    }

	private void sortInstance(final List<Instance> instances) {
		final Comparator<Instance> comparator = new Comparator<Instance>() {
			public int compare(Instance x, Instance y) {
				final String xValue = x.getAttribute(attribute.getName());
				final String yValue = y.getAttribute(attribute.getName());
				final double diff = Double.parseDouble(xValue) - Double.parseDouble(yValue);
				if (diff > 0) return 1;
				else if (diff < 0) return -1;
				else return 0;
			}
		};
		Collections.sort(instances, comparator);
	}

	public String toString() {
		return "Attribute: " + attribute.getName() + "\n" + "ThresholdContinuous: " + threshold + "\n"
				+ "InfoGain: " + infoGain + "\n" + "Subset: " + subset;
	}
}