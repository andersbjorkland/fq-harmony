package util;

/**
 * A small set of helpful methods.
 */
public class Math {

    /**
     * c, speed of light in vacuum, m/s
     */
    public static final int C = 299_792_458;

    /**
     * Calculate frequency
     * @param wavelength, in metres.
     * @return frequency, in Hz.
     */
    public static final double calculateFrequency(double wavelength) {
        return C/wavelength;
    }

    /**
     * Calculate wavelength
     * @param frequency, in Hz
     * @return wavelength, in metres.
     */
    public static final double calculateWavelength(double frequency) {
        return C/frequency;
    }

}
