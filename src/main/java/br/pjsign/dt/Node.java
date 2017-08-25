package br.pjsign.dt;

import java.util.Map;

public interface Node {
    String ROOT ="root";
    String LEAF ="leaf";

    Attribute getAttribute();

    String getTargetLabel();
    void setTargetLabel(String targetLabel);

    String getType();
    void setType(String type);

    Boolean isRoot();
    Boolean isLeaf();

    void addChild(String valueName, Node child);

    Map<String, Node> getChildren();

    Node isTestValue(String value);

    Threshold createThreshold(String keyChild, String value);

    String getValue(final Instance test);

    int getDepth();
    void setDepth(int depth);

    void onTouched(final Instance test);
    int getTouched();
}
