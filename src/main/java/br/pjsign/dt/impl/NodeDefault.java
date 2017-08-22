/*********************************
 * Author: Xue (Charlotte) Lin
 * Date: 2015/04/01
 * 
 * This class defines NodeDefault type of the decision tree,
 * including two types node, root and leaf.
 *********************************/
package br.pjsign.dt.impl;

import java.util.HashMap;
import java.util.Map;

import br.pjsign.dt.Attribute;
import br.pjsign.dt.Node;
import br.pjsign.dt.Threshold;
import br.pjsign.dt.c45.ThresholdContinuous;

public class NodeDefault implements Node {
    private String type;
	private Attribute attribute;
	private HashMap<String, Node> children;
	private String targetLabel;
	
	public NodeDefault(Attribute attribute) {
		this.type = ROOT;
		this.attribute = attribute;
		this.children = new HashMap<String, Node>();
	}
	
	public NodeDefault(String targetLabel) {
		this.type = LEAF;
		this.targetLabel = targetLabel;
	}

	public Attribute getAttribute() {
		return attribute;
	}
	
	public void addChild(String valueName, Node child) {
		children.put(valueName, child);
	}

	public Map<String, Node> getChildren() {
		return children;
	}
	
	public String getTargetLabel() {
		return targetLabel;
	}
	
	public void setTargetLabel(String targetLabel) {
		this.targetLabel = targetLabel;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public Node isTestValue(final String value) {
	    if(!this.attribute.isContinuuousType()) {
	        throw new RuntimeException("Only continuous attribute can be tested");
        }

        for (final String keyChild : children.keySet()) {
            final Threshold threshold = createThreshold(keyChild, value);
            if(threshold.isKeyValid()) {
                return children.get(keyChild);
            }
        }
        throw new RuntimeException("Not found");
	}

    public Boolean isRoot() {
        return getType().equals(ROOT);
    }

    public Boolean isLeaf() {
        return getType().equals(LEAF);
    }

	@Override
	public String toString() {
		if (type.equals("root")) return "Root attribute: " + attribute.getName() + "; Children: " + children;
		else return "Leaf label: " + targetLabel;
	}

	public Threshold createThreshold(String keyChild, String value) {
	    return new ThresholdContinuous(keyChild, value);
    }
}