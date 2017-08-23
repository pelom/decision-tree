/*********************************
 * Author: Xue (Charlotte) Lin
 * Date: 2015/04/01
 *********************************/
package br.pjsign.dt.io;

import java.util.Map;

import br.pjsign.dt.Node;

public class OutPrintTree {

    public static int nodes;
    public static int depth;
    public static int leaf;

	public static StringBuilder print(final Node root) {
	    nodes = -1;
	    depth = -1;
        final StringBuilder sb = print(root, -1);
        sb.append("\ndepth: ").append(((int) depth/2))
                .append(" nodes: ").append(nodes)
                .append(" leaf: ").append(leaf);
        return sb;
	}

    public static StringBuilder print(final Node root, int i) {
        i++;
        nodes++;
        final StringBuilder sb = new StringBuilder();

        if(root.isLeaf()) {
            leaf++;
            sb.append("[").append(root.getTargetLabel()).append("]");
            return sb;
        } else {
            depth++;
            sb.append("\n");
            Map<String, Node> children = root.getChildren();
            for (String valueName : children.keySet()) {
                for( int y = 0; y < i; y++) {
                    sb.append("	");
                }
                sb.append("└── { ").append(i).append(" ");
                sb.append(root.getAttribute().getName());
                sb.append(" (").append(valueName).append(")");

                final StringBuilder st = print(children.get(valueName), i);
                sb.append(st);
                if(st.indexOf("[") == 0) sb.append("\n");
            }
            return sb;
        }
    }
}