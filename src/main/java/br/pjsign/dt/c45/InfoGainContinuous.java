/*********************************
 * Author: Xue (Charlotte) Lin
 * Date: 2015/04/01
 *********************************/
package br.pjsign.dt.c45;

import java.io.IOException;
import java.util.*;

import br.pjsign.dt.*;

public class InfoGainContinuous extends InfoGainAbstract {

    /**
     * @param attribute
     */
	public InfoGainContinuous(Attribute attribute) {
		super(attribute);
	}

	public InfoGain calc(final Attribute target, final List<Instance> instances) throws IOException {
		final String attributeName = this.attribute.getName();
		sortInstance(instances);

		final int thresholdPos = getIndexThreshold(target, instances);
		final Instance instance = instances.get(thresholdPos);

		final String aValue = instance.getAttribute(attributeName);
		final String bValue = instance.getAttribute(attributeName);

		this.threshold = (Double.parseDouble(aValue) + Double.parseDouble(bValue)) / 2;

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

	private int getIndexThreshold(final Attribute target, final List<Instance> instances) throws IOException {
        int thresholdPos = 0;

        for (int i = 0; i < instances.size() - 1; i++) {
        	final Instance instanceA = instances.get(i);
			final Instance instanceB = instances.get(i + 1);

			final String instanceValue = instanceA.getAttribute(attribute.getName());
            final String instanceValue2 = instanceB.getAttribute(attribute.getName());

            if (!instanceValue.equals(instanceValue2)) {
                double currInfoGain = Entropy.calculateContinuous(target, instances, i);
                if (currInfoGain - this.infoGain > 0) {
					this.infoGain = currInfoGain;
                    thresholdPos = i;
                }
            }
        }
        return thresholdPos;
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