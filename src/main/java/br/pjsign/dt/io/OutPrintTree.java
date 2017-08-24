package br.pjsign.dt.io;

import java.util.Map;

import br.pjsign.dt.Node;

public class OutPrintTree {

    private static int nodes;
    private static int leaf;

	public static StringBuilder print(final Node root) {
	    nodes = 0;
        final StringBuilder sb = print(root, 0);
        sb.append(" nodes: ").append(nodes)
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