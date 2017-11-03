package se.femtearenan.fqHarmony.util;

import se.femtearenan.fqHarmony.model.Wave;

/**
 * A new Wave is just one cycle. This class will take a Wave or a bunch and combine them until the
 * specified length is reached, or fuse different waves end to end.
 */
public class WaveCombiner {

    /**
     *
     * @param length of the combined wave in microseconds.
     * @param waves, the waves to be combined.
     * @return
     */
    public static Wave combineInteractingWaves(double length, Wave... waves) {
        Wave combinedWave;
        Wave shortestWave = waves[0];
        Wave longestWave = waves[0];

        for (Wave wave : waves) {
            if (wave.getAmplitude().length < shortestWave.getAmplitude().length) {
                shortestWave = wave;
            } else if (wave.getAmplitude().length > longestWave.getAmplitude().length) {
                longestWave = wave;
            }
        }

        combinedWave = new Wave(longestWave.getWavelength());
        double[] combinedAmps = new double[(int)(length * Wave.RESOLUTION_CELLS_PER_METERS)];
        initializeEmptyAmpArray(combinedAmps);

        for (Wave wave : waves) {
            double[] amps = wave.getAmplitude();
            int ampsIndex = 0;
            int loops = 0;
            for (int i = 0; i < combinedAmps.length; i++) {
                // Check if index is equal to the length of the array that is being copied, or any of its multiples.
                // Cycle through from the beginning of the copied array if that is the case.
                if (i == amps.length || i == amps.length * (loops + 1)) {
                    ampsIndex = 0;
                    loops += 1;
                }
                combinedAmps[i] += amps[ampsIndex];
                ampsIndex++;
            }
        }

        combinedWave.setAmplitude(combinedAmps);

        return combinedWave;
    }

    public static Wave fuseWaves(Wave... waves) {
        int totalLength = 0;
        for (Wave wave : waves) {
            totalLength += wave.getAmplitude().length;
        }
        double[] fusedAmps = new double[totalLength];

        int i = 0;
        for (Wave wave : waves) {
            for (double amp : wave.getAmplitude()) {
                fusedAmps[i] = amp;
                i++;
            }
        }

        Wave fusedWave = new Wave(waves[0].getWavelength());
        fusedWave.setAmplitude(fusedAmps);
        return fusedWave;
    }

    private static void initializeEmptyAmpArray(double[] amps) {
        for (int i = 0; i < amps.length; i++) {
            amps[i] = 0;
        }
    }
}
