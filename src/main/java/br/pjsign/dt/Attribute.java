package br.pjsign.dt;

import java.util.List;

/**
 * @author andreleite
 */

public interface Attribute {
    static final String REAL_VALUE = "real";

    static final String CONTINUOUS_TYPE  = "continuous";
    static final String DISCRETE_TYPE    = "discrete";

    String getName();

    String getType();

    Boolean isContinuuousType();
    Boolean isDiscreteType();
    List<String> getValues();
}