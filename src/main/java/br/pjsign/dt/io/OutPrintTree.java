package br.pjsign.dt.io;

import java.util.Map;

import br.pjsign.dt.Attribute;
import br.pjsign.dt.DTree;
import br.pjsign.dt.Node;

public class OutPrintTree {

    private static boolean showchild = false;
    private static boolean showAttribute = false;

    public static StringBuilder print(final DTree tree) {
        return print(tree, false , false);
    }

	public static StringBuilder print(final DTree tree, boolean showAttribute, boolean showchilds) {
	    setShowAttribute(showAttribute);
	    setShowchild(showchilds);
	    final StringBuilder sb = print(tree.getRoot(), 0);
        sb.append("\n").append(" nodes: ").append(tree.lengthNodes())
                .append(" leaf: ").append(tree.lengthNodesLeft());
        return sb;
	}

    public static StringBuilder print(final Node root, int i) {
        i++;
        final StringBuilder sb = new StringBuilder();

        if(root.isLeaf()) {
            sb.append("[").append(root.getTargetLabel()).append("]");
            return sb;
        }

        printRoot(root, "", sb);

        final Map<String, Node> children = root.getChildren();

        for (String keyNameChild : children.keySet()) {

            final Node child = children.get(keyNameChild);
            final StringBuilder st = print(child, i);

            printRoot(child, keyNameChild, sb);
            sb.append(st);
        }
        return sb;

    }

    private static void printRoot(Node root, String keyNode, StringBuilder sb) {
	    if(root.getDepth() != 0 && keyNode.length() == 0) {
	        return;
        }
        sb.append("\n");
        for( int y = 0; y < root.getDepth(); y++) {
            sb.append("	");
        }

        sb.append("└── { ").append(root.getDepth());
        sb.append(" <").append(keyNode).append("> ");
        if(root.getAttribute() != null && showAttribute) {
            sb.append(root.getAttribute().getName());
        }

        if(root.getChildren() != null && showchild) {
            final StringBuilder attSb = createStartListSb();
            final StringBuilder keySb = createStartListSb();

            for (String keyChild: root.getChildren().keySet()) {
                final Node nodeChild = root.getChildren().get(keyChild);

                keySb.append(keyChild).append(",");

                if(nodeChild.getAttribute() != null) {
                    final Attribute att = nodeChild.getAttribute();
                    attSb.append(att.getName()).append(",");
                }

            }
            createEndListSb(attSb);
            createEndListSb(keySb);

            sb.append(keySb).append(attSb);
        }
        sb.append(" (").append(root.getTouched()).append(") ");
    }

    private static StringBuilder createStartListSb() {
        final StringBuilder sb = new StringBuilder();
        sb.append(" [");
        return sb;
    }

    private static StringBuilder createEndListSb(final StringBuilder sb) {
        sb.deleteCharAt(sb.length()-1).append("]");
        return sb;
    }

    public static void setShowchild(boolean showchild) {
        OutPrintTree.showchild = showchild;
    }

    public static void setShowAttribute(boolean showAttribute) {
        OutPrintTree.showAttribute = showAttribute;
    }
}