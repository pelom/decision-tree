/*
 * Author: Charlotte Lin
 * Date: 2015/3/31
 */

package br.pjsign.dt.impl;

import br.pjsign.dt.Attribute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AttributeDefault implements Attribute {

    // Field name: name of the attribute
	private String name;
	
	// Field type: discrete value or continuous value 
	private String type;
	
	// Field values: values. If discrete, stores values; if continuous, stores "real"
	private List<String> values;
	
	public AttributeDefault(String name, String values) throws IOException {
		this.name = name;

		if(isValueReal(values)) {
		    this.type = CONTINUOUS_TYPE;
            this.values = new ArrayList<String>();
            this.values.add(REAL_VALUE);
        } else {
		    this.type = DISCRETE_TYPE;
            this.values = constructValues(values);
        }

	}

    public Boolean isContinuuousType() {
        return getType().equals(Attribute.CONTINUOUS_TYPE);
    }

    public Boolean isDiscreteType() {
        return getType().equals(Attribute.DISCRETE_TYPE);
    }

	private boolean isValueReal(final String values) {
	    return values.equalsIgnoreCase(REAL_VALUE);
    }

    private List<String> constructValues(String s) throws IOException {
        if (s == null || s.length() < 2) {
            throw new IOException("Invalid input format");
        }
        s = s.substring(1, s.length() - 1);
        final String[] sArr = s.split(",");
        final List<String> values = new ArrayList<String>();
        for (String item : sArr) {
            values.add(item.trim());
        }
        return values;
    }

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public List<String> getValues() {
		return values;
	}

    @Override
    public String toString() {
        return "AttributeDefault{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", values=" + values +
                '}';
    }
}