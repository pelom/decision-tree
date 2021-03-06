package br.pjsign.dt.impl.c45;

public class ThresholdContinuous extends ThresholdDefault {

    public ThresholdContinuous(final String keyChild, final String value) {
        super(keyChild, value);
    }
    public Boolean isKeyValid() {
        return thresholdLabel.equals(keyLabel);
    }

    public static String generateLess(double threshold) {
        return generateKey(LESS_LABEL, threshold);
    }

    public static String generateMore(double threshold) {
        return generateKey(MORE_LABEL, threshold);
    }

    private static String generateKey(final String keyThreshold, double threshold) {
        return keyThreshold + Double.toString(threshold);
    }
}
