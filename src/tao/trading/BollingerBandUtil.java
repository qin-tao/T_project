package tao.trading;

import java.util.ArrayList;
import java.util.List;

public class BollingerBandUtil {

    public static class BollingerBand {
        public Double middle; // SMA
        public Double upper;  // SMA + k * std
        public Double lower;  // SMA - k * std

        public BollingerBand(Double middle, Double upper, Double lower) {
            this.middle = middle;
            this.upper = upper;
            this.lower = lower;
        }
    }

    /**
     * Calculates Bollinger Bands.
     *
     * @param quotes      List of QuoteData (chronological: oldest → newest)
     * @param period      Period for SMA (typical 20)
     * @param numStdDev   Number of standard deviations (typical 2)
     * @return List of BollingerBand objects
     */
    public static List<BollingerBand> calculateBollingerBands(
            List<QuoteData> quotes,
            int period,
            double numStdDev) {

        List<BollingerBand> bands = new ArrayList<>();
        int size = quotes.size();

        if (size < period) {
            return bands;
        }

        for (int i = 0; i < size; i++) {
            if (i < period - 1) {
                bands.add(new BollingerBand(null, null, null));
                continue;
            }

            double sum = 0.0;
            for (int j = i - period + 1; j <= i; j++) {
                sum += quotes.get(j).close;
            }
            double sma = sum / period;

            // calculate standard deviation
            double varianceSum = 0.0;
            for (int j = i - period + 1; j <= i; j++) {
                double diff = quotes.get(j).close - sma;
                varianceSum += diff * diff;
            }
            double stdDev = Math.sqrt(varianceSum / period);

            double upper = sma + numStdDev * stdDev;
            double lower = sma - numStdDev * stdDev;

            bands.add(new BollingerBand(sma, upper, lower));
        }

        return bands;
    }
}