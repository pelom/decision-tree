package br.pjsign.dt;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DTree {

    Node construct(List<Instance> instances) throws IOException;

    Node getRoot();

    Map<Integer, Node> mineInstances(final List<Instance> testInstances);

    int lengthNodes();
    int lengthNodesLeft();
}
