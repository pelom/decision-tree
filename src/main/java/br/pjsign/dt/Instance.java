package br.pjsign.dt;

import java.util.Collection;

public interface Instance {

    int getInstanceIndex();

    String getAttribute(String name);

    void setAttribute(String name, String value);

    Collection<String> getAllAttribute();
}
