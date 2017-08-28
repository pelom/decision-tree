package br.pjsign.dt.impl.c45;

import br.pjsign.dt.Threshold;

public abstract class ThresholdDefault implements Threshold {
    protected String keyThreshold;
    protected String keyLabel;
    protected String thresholdLabel;

    public ThresholdDefault(final String keyChild, final String value) {
        this.keyThreshold = keyChild.substring(LESS_LABEL.length());
        this.keyLabel = keyChild.substring(0, LESS_LABEL.length());
        this.thresholdLabel = isValueLess(value) ? LESS_LABEL : MORE_LABEL;
    }

    public String getKeyThreshold() {
        return keyThreshold;
    }

    public String getKeyLabel() {
        return keyLabel;
    }

    public String getThresholdLabel() {
        return thresholdLabel;
    }

    public Boolean isValueLess(final String value) {
        return Double.parseDouble(value) < Double.parseDouble(this.keyThreshold);
    }

    public Boolean isValueMore(final String value) {
        return Double.parseDouble(value) >= Double.parseDouble(this.keyThreshold);
    }
}