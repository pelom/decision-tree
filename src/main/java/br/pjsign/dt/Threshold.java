package br.pjsign.dt;

public interface Threshold {
    public static final String LESS_LABEL = "less";
    public static final String MORE_LABEL = "more";
    String getKeyThreshold();
    String getKeyLabel();
    String getThresholdLabel();
    Boolean isKeyValid();
    Boolean isValueLess(final String value);
    Boolean isValueMore(final String value);
}
