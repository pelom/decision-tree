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
import br.pjsign.dt.Instance;
import br.pjsign.dt.Node;
import br.pjsign.dt.Threshold;
import br.pjsign.dt.impl.c45.ThresholdContinuous;

public class NodeDefault implements Node {
    private String type;
	private Attribute attribute;
	private HashMap<String, Node> children;
	private String targetLabel;
	private int depth;
    private int touch;

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
	    if (this.attribute.isDiscreteType()) {
            return getChildren().get(value);
        }

        for (final String keyChild : this.children.keySet()) {
            final Threshold threshold = createThreshold(keyChild, value);
            if(threshold.isKeyValid()) {
                return this.children.get(keyChild);
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
        return "NodeDefault{" +
                "type='" + type + '\'' +
                ", depth=" + depth +
                ", children=" + (children != null ? children.size() : 0) +
                ", targetLabel='" + targetLabel + '\'' +
                ", touch='" + touch + '\'' +
                ", attribute=" + attribute +
                ", children=" + children +
                '}';
    }

    public Threshold createThreshold(String keyChild, String value) {
	    return new ThresholdContinuous(keyChild, value);
    }

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

    public String getValue(final Instance test) {
        final Attribute attribute = getAttribute();
        return test.getAttribute(attribute.getName());
    }

    public void onTouched(final Instance test) {
        this.touch +=1;
    }

    public int getTouched() {
        return this.touch;
    }
}