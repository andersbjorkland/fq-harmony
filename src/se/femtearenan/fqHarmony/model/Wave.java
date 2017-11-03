package se.femtearenan.fqHarmony.model;

/**
 * A wave consisting of a sinus wave in the form of an array with default amplitude stored as a double value between
 * -1 and 1, as well as a wavelength.
 */

public class Wave {
    public final static int RESOLUTION_CELLS_PER_METERS = 10_000;
    private double radiansUnit;
    private double[] amplitude;
    private double wavelength;
    private double amplitudeMultiplier;
    private int phase;

    /**
     *
     * @param wavelength
     */
    public Wave(double wavelength) {
        this(wavelength, 0);
    }

    /**
     *
     * @param wavelength
     * @param phase
     */
    public Wave(double wavelength, int phase) {
        this(wavelength, phase, 1);
    }

    /**
     *
     * @param wavelength
     * @param amplitudeMultiplier
     */
    public Wave(double wavelength, double amplitudeMultiplier) {
        this(wavelength, 0, amplitudeMultiplier);
    }

    /**
     *
     * @param wavelength in meters.
     * @param phase in degrees.
     * @param amplitudeMultiplier
     */
    public Wave(double wavelength, int phase, double amplitudeMultiplier) {

        // Determine that the wavelength will have an accuracy of 1/RESOLUTION_CELLS_PER_METERS.
        double roundedWavelength = Math.round(wavelength * RESOLUTION_CELLS_PER_METERS);
        roundedWavelength = roundedWavelength / RESOLUTION_CELLS_PER_METERS;

        this.wavelength = roundedWavelength;
        this.phase = Math.abs(phase);
        this.amplitudeMultiplier = amplitudeMultiplier;
        if (this.phase > 359) {
            this.phase = this.phase % 360;
        }
        amplitude = new double[(int)Math.round(RESOLUTION_CELLS_PER_METERS * wavelength)];
        radiansUnit = (2 * Math.PI) / amplitude.length;
        initializeAmplitudeForOneCycle(amplitudeMultiplier);
        phaseShift(this.phase);
    }

    private void initializeAmplitudeForOneCycle(double amplitudeMultiplier) {
        for (int i = 0; i < amplitude.length; i++) {
            amplitude[i] = Math.sin(radiansUnit * i) * amplitudeMultiplier;
        }
    }

    public void phaseShift(int phaseShift) {
        if (phaseShift >= 360) {
            phaseShift = phaseShift % 360;
        }
        if (phaseShift > 0) {
            double[] shiftedAmplitude = new double[amplitude.length];
            int shiftedIndex = (int) ((double)amplitude.length * ((double)phaseShift / 360));
            for (int i = 0; i < amplitude.length; i++) {
                shiftedAmplitude[i] = amplitude[shiftedIndex];
                if (shiftedIndex == amplitude.length - 1) {
                    shiftedIndex = 0;
                } else {
                    shiftedIndex++;
                }
            }
            amplitude = shiftedAmplitude;
        }
    }

    public void printAmplitude() {
        System.out.println("Here are the values of the amplitude array: \n");
        for (int i = 0; i < amplitude.length; i++) {
            System.out.println(i + ": " + amplitude[i]);
        }
    }

    public double[] getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double[] amps) {
        this.amplitude = amps;
    }

    public void amplifyAmplitude(double amplify) {
        initializeAmplitudeForOneCycle(amplify);
        /*
        for (int i = 0; i < amplitude.length; i++) {
            amplitude[i] = amplitude[i] * amplify;
        }
        */
    }

    public double getWavelength() {
        return wavelength;
    }
}
