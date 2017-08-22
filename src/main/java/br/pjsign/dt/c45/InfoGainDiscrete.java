/*********************************
 * Author: Xue (Charlotte) Lin
 * Date: 2015/04/01
 *********************************/
package br.pjsign.dt.c45;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.pjsign.dt.*;

public class InfoGainDiscrete extends InfoGainAbstract implements InfoCalc {

    public InfoGainDiscrete(final Attribute attribute) {
        super(attribute);
    }

    public InfoGain calc(final Attribute target, final List<Instance> instances) throws IOException {

		//final List<String> valuesOfAttribute = this.attribute.getValues();
		//final String attributeName = ;

		this.subset = new HashMap<String, List<Instance>>();
		for (String val : this.attribute.getValues()) {
			this.subset.put(val, new ArrayList<Instance>());
		}

		for (Instance instance : instances) {
			final String valueOfInstanceAtAttribute = instance.getAttribute(this.attribute.getName());
			//HashMap<String, String> attributeValuePairsOfInstance = instance.getAttributeValuePairs();
			//String valueOfInstanceAtAttribute = attributeValuePairsOfInstance.get(attributeName);
			if (!subset.containsKey(valueOfInstanceAtAttribute))
				throw new IOException("Invalid input data");

			this.subset.get(valueOfInstanceAtAttribute).add(instance);
		}

		int totalN = instances.size();
		this.infoGain = Entropy.calculate(target, instances);

        for (List<Instance> collection: subset.values()) {
            int subN = collection.size();
            this.infoGain -= ( (double) subN ) / ( (double) totalN ) *
                    Entropy.calculate(target, collection);
        }

//		for (String s : subset.keySet()) {
//			final List<Instance> currSubset = subset.get(s);
//			int subN = currSubset.size();
//			double subRes = ( (double) subN ) / ( (double) totalN ) *
//					Entropy.calculate(target, currSubset);
//            this.infoGain -= subRes;
//		}

		return this;
	}

}