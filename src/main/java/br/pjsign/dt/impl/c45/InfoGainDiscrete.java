/*********************************
 * Author: Xue (Charlotte) Lin
 * Date: 2015/04/01
 *********************************/
package br.pjsign.dt.impl.c45;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.pjsign.dt.*;

public class InfoGainDiscrete extends InfoGainAbstract {

    public InfoGainDiscrete(final Attribute attribute) {
        super(attribute);
    }

    public InfoGain calc(final Attribute target, final List<Instance> instances) throws IOException {
        initSubset(instances);

		int totalN = instances.size();
		this.infoGain = Entropy.calculate(target, instances);

        for (final List<Instance> collection: this.subset.values()) {
            int subN = collection.size();
            this.infoGain -= ( (double) subN ) / ( (double) totalN ) *
                    Entropy.calculate(target, collection);
        }

		return this;
	}

	private void initSubset(final List<Instance> instances) throws IOException {
        for (String val : this.attribute.getValues()) {
            this.subset.put(val, new ArrayList<Instance>());
        }

        for (final Instance instance : instances) {
            final String valueOfInstanceAtAttribute = instance.getAttribute(this.attribute.getName());

            if (!subset.containsKey(valueOfInstanceAtAttribute)) {
                throw new IOException("Invalid input data");
            }

            this.subset.get(valueOfInstanceAtAttribute).add(instance);
        }
    }
}