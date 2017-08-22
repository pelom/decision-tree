/*
 * Author: Charlotte Lin
 * Date: 2015/3/31
 * 
 */
package br.pjsign.dt.impl;

import br.pjsign.dt.Instance;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InstanceDefault implements Instance {
	private static int count = 0;
	private int index;
	private Map<String, String> attributeValuePairs;

	public InstanceDefault() {
		index = count;
		attributeValuePairs = new HashMap<String, String>();
		count++;
	}

    public int getInstanceIndex() {
        return index;
    }

	public String getAttribute(String name) {
        return this.attributeValuePairs.get(name);
    }

	public void setAttribute(String name, String value) {
	    this.attributeValuePairs.put(name, value);
    }

    public Collection<String> getAllAttribute() {
       return this.attributeValuePairs.values();
    }

	@Override
	public String toString() {
		return "InstanceDefault{" +
				"index=" + index +
				", attributeValuePairs=" + attributeValuePairs +
				'}';
	}
}